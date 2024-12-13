require 'socket'
require 'google/protobuf'
require_relative 'dist_servers_pb'

class AdminPanel
  SERVER_PORTS = [6001, 6002, 6003]

  def initialize
    @config = read_config
    @server_sockets = {}
    @plotter_socket = nil

    connect_to_plotter
    connect_to_servers
    start_servers
    perform_capacity_checks
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
    begin
      @plotter_socket = TCPSocket.new('localhost', 6000)
      puts "Plotter sunucusuna bağlanıldı"
    rescue => e
      puts "Plotter sunucusuna bağlantı başarısız: #{e.message}"
    end
  end

  # Sunuculara bağlan
  def connect_to_servers
    SERVER_PORTS.each do |port|
      connected = false
      until connected
        begin
          socket = TCPSocket.new('localhost', port)
          @server_sockets[port] = socket
          connected = true
          puts "Port #{port} ile bağlantı kuruldu"
        rescue => e
          puts "Port #{port} ile bağlantı başarısız: #{e.message}. Yeniden denenecek..."
          sleep(2)
        end
      end
    end
  end

  # Sunucuları başlat
  def start_servers
    config = DistServers::Message.new(demand: :STRT)
    @server_sockets.each do |port, socket|
      begin
        socket.write(config.to_proto)
        puts "Gönderildi.."
        response = DistServers::Message.decode(socket.read(1024))
        puts "Alındı.."
        handle_response(port, response)
      rescue => e
        puts "Port #{port} için başlatma hatası: #{e.message}"
      end
    end
  end

  # Kapasite sorgularını yap ve Plotter'a gönder
  def perform_capacity_checks
    loop do
      sleep(5)
      send_capacity_requests
      sleep(1)
      send_capacity_to_plotter
    end
  end  

  # Kapasite sorguları gönder ve yanıtları işle
  def send_capacity_requests
    message = DistServers::Message.new(demand: :CPCTY)
    @server_sockets.each do |port, socket|
      begin
        socket.write(message.to_proto)
        response = socket.read(1024)
        next if response.nil? || response.empty?

        capacity_response = DistServers::Capacity.decode(response)
        puts "Port #{port}: Kapasite Durumu: #{capacity_response.serverXStatus}, Zaman: #{capacity_response.timestamp}"
      rescue => e
        puts "Port #{port} için kapasite sorgusu hatası: #{e.message}"
      end
    end
  end

  # Kapasite bilgilerini Plotter'a gönder
  def send_capacity_to_plotter
    return unless @plotter_socket

    @server_sockets.each do |port, socket|
      begin
        message = DistServers::Message.new(demand: :CPCTY)
        socket.write(message.to_proto)
        response = socket.read(1024)
        next if response.nil? || response.empty?

        capacity_response = DistServers::Capacity.decode(response)
        @plotter_socket.write(capacity_response.to_proto)
        puts "Port #{port}: Kapasite bilgisi Plotter'a gönderildi"
      rescue => e
        puts "Port #{port} için Plotter'a veri gönderim hatası: #{e.message}"
      end
    end
  end

  # Yanıtları işleme metodu
  def handle_response(port, response)
    case response.response
    when :YEP
      puts "Port #{port}: Sunucu başarıyla başlatıldı."
    when :NOP
      puts "Port #{port}: Sunucu başlatılamadı."
    else
      puts "Port #{port}: Bilinmeyen yanıt alındı."
    end
  end

  # Bağlantıları kapat
  def close_connections
    @server_sockets.each_value(&:close)
    @plotter_socket&.close
  end
end

# AdminPanel başlat
admin_panel = AdminPanel.new

loop do
  puts "Çıkmak için 'exit' yazın."
  break if gets.chomp == 'exit'
end

admin_panel.close_connections
