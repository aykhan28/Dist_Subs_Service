require 'socket'
require 'google/protobuf'
require_relative 'dist_servers_pb'
#require_relative 'Capacity_pb'

class AdminPanel
  SERVER_PORTS = [5001, 5002, 5003]
  SERVERS = []
  PLOTTER_SOCKET = nil

  def initialize
    read_config
    connect_to_servers
    #connect_to_plotter
    start_servers
    handle_capacity_requests
  end

  def read_config
    @config = {}
    File.foreach('dist_subs.conf') do |line|
      key, value = line.split('=').map(&:strip)
      @config[key] = value
    end
  end

  def connect_to_servers
    SERVER_PORTS.each do |port|
      begin
        socket = TCPSocket.new('localhost', port)
        SERVERS << socket
        puts "Port #{port} ile bağlantı kuruldu"
      rescue => e
        puts "Port #{port} ile bağlantı kurulamadı: #{e.message}"
      end
    end
  end

  #def connect_to_plotter
  #  begin
  #    @plotter_socket = TCPSocket.new('localhost', 6000)
  #    puts "Plotter sunucusuna bağlanıldı"
  #  rescue => e
  #    puts "Plotter sunucusuna bağlantı başarısız: #{e.message}"
  #    @plotter_socket = nil
  #  end
  #end

  def send_message_to_all_servers(message)
    socket.puts("ADMIN")

    SERVERS.each do |socket|
      socket.write(message.to_proto)
      response = DistServers::Message.decode(socket.read)
      handle_response(response)
    end
  end

  def start_servers
    config = DistServers::Configuration.new(
      fault_tolerance_level: @config['fault_tolerance_level'].to_i,
      method: :STRT
    )
    send_message_to_all_servers(config)
  end

  #def send_capacity_request
  #  message = DistServers::Message.new(demand: :CPCTY)
  #  SERVERS.each do |socket|
  #    socket.write(message.to_proto)
  #    response = socket.read
  #    handle_capacity_response(response)
  #  end
  #end

  #def handle_capacity_response(response)
  #  return unless @plotter_socket
  #  data = DistServers::Capacity.decode(response)
  #  @plotter_socket.write(data.to_proto)
  #end
end

admin_panel = AdminPanel.new

loop do
  puts "Çıkmak için 'exit' yazın."
  break if gets.chomp == 'exit'
end

admin_panel.close_connections
