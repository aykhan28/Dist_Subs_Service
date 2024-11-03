require 'socket'
require 'google/protobuf'
require_relative 'dist_servers_pb'

class AdminPanel
  SERVER_PORTS = [5001, 5002, 5003]
  SERVERS = []

  def initialize
    read_config
    connect_to_servers
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

  def start_servers
    config = DistServers::Configuration.new(
      fault_tolerance_level: @config['fault_tolerance_level'].to_i,
      method: :STRT
    )
    send_message_to_all_servers(config)
  end

  def handle_capacity_requests
    loop do
      sleep 5
      send_capacity_request
    end
  end

  def send_capacity_request
    message = DistServers::Message.new(demand: :CPCTY)
    send_message_to_all_servers(message)
  end

  def send_message_to_all_servers(message)
    SERVERS.each do |socket|
      socket.write(message.to_proto)
      response = DistServers::Message.decode(socket.read)
      handle_response(response)
    end
  end

  def handle_response(response)
    case response.response
    when :YEP
      puts "Yanıt: Evet"
    when :NOP
      puts "Yanıt: Hayır"
    else
      puts "Bilinmeyen yanıt"
    end
  end

  def close_connections
    SERVERS.each(&:close)
  end
end

admin_panel = AdminPanel.new

loop do
  puts "Çıkmak için 'exit' yazın."
  break if gets.chomp == 'exit'
end

admin_panel.close_connections
