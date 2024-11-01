require 'socket'
require 'protobuf'
require_relative 'Configuration_pb'
require_relative 'Message_pb'
require_relative 'Capacity_pb'

CONF = "dist_subs.conf"
SERVER_PORTS = [5001, 5002, 5003]

def read_config_file
  value = 0
  File.open(CONF, 'r') do |file|
    file.each_line do |line|
      arg = line.split('=', 2)
      value = arg[1].to_i
    end
  end
  value
end

def handle_jserver(recv, value)
  # Konfiqurasyon

  config_message = Configuration.new(fault_tolerance_level: value, method_type: Configuration::MethodType::STRT)
  recv.write(config_message.serialize_to_string)
  puts "Konfigürasyon mesaji gönderildi"

  response_data = recv.read
  response_message = Message.decode(response_data)
  puts "Alındı: #{response_message.demand}, #{response_message.response}"

  # Kapasite:

  capacity_request = Message.new(demand: Message::Demand::CPCTY, response: Message::Response::NULL)
  recv.write(capacity_request.serialize_to_string)
  puts "Kapasite sorgusu gönderildi"


  capacity_data = recv.read
  capacity_message = Capacity.decode(capacity_data)
  puts "Alındı: #{capacity_message.serverX_status}, #{capacity_message.timestamp}"
end

fault_tolerance_value = read_config_file

SERVER_PORTS.each do |port|
  begin
    socket = TCPSocket.open("localhost", port)
    puts "Bağlantı kuruldu: #{port}"
    handle_jserver(socket, fault_tolerance_value)
    socket.close
  rescue Errno::ECONNREFUSED
    puts "Bağlanamadı: #{port}"
  end
end
