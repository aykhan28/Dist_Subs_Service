require 'socket'
require 'google/protobuf'
require_relative 'dist_servers_pb'

class AdminPanel
  SERVER_PORTS = [6001, 6002, 6003]
  PLOTTER_PORT = 6000

  def initialize
    @servers = []
    @plotter_socket = nil
    connect_to_servers
    connect_to_plotter
    handle_capacity_requests
  end

  def connect_to_servers
    SERVER_PORTS.each do |port|
      begin
        socket = TCPSocket.new('localhost', port)
        @servers << socket
        puts "Port #{port} ile bağlantı kuruldu"
      rescue => e
        puts "Port #{port} ile bağlantı başarısız. Yeniden denenecek..."
        sleep(2)
        retry
      end
    end
  end

  # Plotter'a bağlan
  def connect_to_plotter
    begin
      @plotter_socket = TCPSocket.new('localhost', PLOTTER_PORT)
      puts "Plotter sunucusuna bağlanıldı"
    rescue => e
      puts "Plotter sunucusuna bağlantı başarısız: #{e.message}"
    end
  end

  def handle_capacity_requests
    loop do
      sleep(5)
      send_capacity_request
    end
  end

  def send_capacity_request
    message = "CPCTY" # Kapasite sorgusu
    @servers.each_with_index do |socket, index|
      begin
        socket.puts(message)

        response_data = socket.read(1024)
        capacity = Capacity.decode(response_data)

        puts "Sunucu Portu: #{SERVER_PORTS[index]}, Abone Sayısı: #{capacity.subscriber_count}"

        send_to_plotter(capacity)
      rescue => e
        puts "Sunucudan kapasite bilgisi alınamadı: #{e.message}"
      end
    end
  end

  def send_to_plotter(capacity)
    if @plotter_socket
      begin
        @plotter_socket.write(capacity.to_proto)
        puts "Plotter'a kapasite bilgisi gönderildi: #{capacity.subscriber_count}"
      rescue => e
        puts "Plotter'a kapasite bilgisi gönderilemedi: #{e.message}"
      end
    else
      puts "Plotter sunucusuna bağlı değil."
    end
  end

  def close_connections
    @servers.each(&:close)
    @plotter_socket&.close
  end
end

admin_panel = AdminPanel.new

loop do
  puts "Çıkmak için 'exit' yazın."
  break if gets.chomp == 'exit'
end

admin_panel.close_connections
