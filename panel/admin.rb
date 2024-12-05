require 'socket'
require 'google/protobuf'
require_relative 'dist_servers_pb'

class AdminPanel
  SERVER_PORTS = [6001, 6002, 6003]
  SERVERS = []
  @plotter_socket = nil

  def initialize
    @config = read_config
    @server_mutex = Mutex.new
    connect_to_plotter
    connect_to_servers
    start_servers_thread
    capacity_request_thread
  end

  # Konfigürasyon dosyasını oku
  def read_config
    config = {}
    File.foreach('dist_subs.conf') do |line|
      key, value = line.split('=').map(&:strip)
      config[key] = value
    end
    config
  end

  # Plotter'a bağlan
  def connect_to_plotter
    Thread.new do
      begin
        @plotter_socket = TCPSocket.new('localhost', 6000)
        puts "Plotter sunucusuna bağlanıldı"
      rescue => e
        puts "Plotter sunucusuna bağlantı başarısız: #{e.message}"
      end
    end
  end

  # Sunuculara bağlan
  def connect_to_servers
    SERVER_PORTS.each do |port|
      Thread.new do
        connected = false
        until connected
          begin
            socket = TCPSocket.new('localhost', port)
            @server_mutex.synchronize { SERVERS << socket }
            connected = true
            puts "Port #{port} ile bağlantı kuruldu"
          rescue => e
            puts "Port #{port} ile bağlantı başarısız: #{e.message}. Yeniden denenecek..."
            sleep(2)
          end
        end
      end.join # Sunucular sırayla bağlanmalı
    end
  end

  # Sunucuları başlatan thread
  def start_servers_thread
    Thread.new do
      config = DistServers::Configuration.new(
        fault_tolerance_level: @config['fault_tolerance_level'].to_i,
        method: :STRT
      )
      send_message_to_all_servers(config)
    end
  end

  # Kapasite sorgularını düzenli aralıklarla gönderen thread
  def capacity_request_thread
    Thread.new do
      loop do
        sleep(5)
        send_capacity_request
      end
    end
  end

  # Tüm sunuculara mesaj gönder
  def send_message_to_all_servers(message)
    SERVERS.each do |socket|
      begin
        socket.write(message.to_proto)
        response = DistServers::Message.decode(socket.read(1024))
        handle_response(response)
      rescue => e
        puts "Mesaj gönderimi sırasında hata oluştu: #{e.message}"
      end
    end
  end

  # Yanıtları işleyen metot
  def handle_response(response)
    case response.response
    when :YEP
      puts "Sunucudan yanıt: Başarıyla başlatıldı."
    when :NOP
      puts "Sunucudan yanıt: İşlem başarısız."
    else
      puts "Bilinmeyen yanıt alındı."
    end
  end

  # Kapasite sorgusu gönder
  def send_capacity_request
    message = DistServers::Message.new(demand: :CPCTY)
    SERVERS.each do |socket|
      begin
        socket.write(message.to_proto)
        response = socket.read(1024)
        next if response.nil? || response.empty?

        capacity_response = DistServers::Capacity.decode(response)
        handle_capacity_response(capacity_response)
      rescue => e
        puts "Sunucudan kapasite bilgisi alınamadı: #{e.message}"
      end
    end
  end

  # Gelen kapasite yanıtını işle
  def handle_capacity_response(capacity)
    puts "Sunucudan gelen kapasite durumu: #{capacity.serverXStatus}, Zaman: #{capacity.timestamp}"
    if @plotter_socket
      @plotter_socket.write(capacity.to_proto)
    end
  end

  # Bağlantıları kapat
  def close_connections
    SERVERS.each(&:close)
    @plotter_socket&.close
  end
end

admin_panel = AdminPanel.new

loop do
  puts "Çıkmak için 'exit' yazın."
  break if gets.chomp == 'exit'
end

admin_panel.close_connections
