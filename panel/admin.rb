require_relative 'message_pb'
require_relative 'configuration_pb'
require_relative 'capacity_pb'
require 'socket'

JAVA_SERVER_PORTS = [5001, 5002, 5003]

# Config dosyasını okuyarak hata tolerans seviyesini döner
def load_configuration(conf)
  fault_tolerance_level = 0
  begin
    File.open(conf, 'r') do |file|
      file.each_line do |line|
        fault_tolerance_level = line.split('=', 2).last.to_i
      end
    end
  rescue Errno::ENOENT
    puts "Config dosyası bulunamadı."
  rescue => e
    puts "Config dosyası okunurken hata oluştu: #{e.message}"
  end
  fault_tolerance_level
end

# Java sunucularına bağlantı sağlar
def establish_java_connections
  connections = {}
  JAVA_SERVER_PORTS.each_with_index do |port, index|
    server_id = index + 1
    begin
      socket = TCPSocket.new("localhost", port)
      puts "Java sunucu #{server_id} bağlantısı başarılı (Port: #{port})"
      connections[server_id] = socket
    rescue Errno::ECONNREFUSED
      puts "Java sunucu #{server_id} (Port: #{port}) bağlantı hatası."
    rescue => e
      puts "Java sunucu #{server_id} bağlantı hatası: #{e.message}"
    end
  end
  connections
end

# Python sunucusuna bağlantı sağlar
def establish_python_connection
  begin
    socket = TCPSocket.new("localhost", 6000)
    puts "Python sunucusuna bağlantı başarılı"
    socket
  rescue Errno::ECONNREFUSED
    puts "Python sunucusu bağlantı hatası."
  rescue => e
    puts "Python sunucusu bağlantı hatası: #{e.message}"
    nil
  end
end

# Java sunucusuna konfigürasyon gönderir ve yanıt alır
def transmit_configuration(socket, fault_tolerance_level)
  begin
    config = Configuration.new(fault_tolerance_level: fault_tolerance_level, method: MethodType::STRT)
    socket.write([config.to_proto.size].pack("N") + config.to_proto)
    puts "Konfigürasyon gönderildi."

    response_length = socket.read(4).unpack("N")[0]
    response_proto = socket.read(response_length)
    response = Message.decode(response_proto)
    puts "Yanıt alındı: #{response.demand}, #{response.response}"

    response
  rescue IOError
    puts "Sunucu bağlantı hatası."
  rescue => e
    puts "Konfigürasyon gönderim hatası: #{e.message}"
  end
end

# Java sunucusundan kapasite bilgisi talep eder
def request_server_capacity(socket, server_id)
  begin
    request = Message.new(demand: Demand::CPCTY)
    socket.write([request.to_proto.size].pack("N") + request.to_proto)
    puts "Kapasite talebi gönderildi."

    capacity = Capacity.new(server_id: server_id, timestamp: Time.now.to_i)
    socket.write([capacity.to_proto.size].pack("N") + capacity.to_proto)
    puts "Kapasite bilgisi gönderildi."

    response_length = socket.read(4).unpack("N")[0]
    response_proto = socket.read(response_length)
    response = Capacity.decode(response_proto)
    puts "Kapasite alındı: Server#{response.server_id}, #{response.serverX_status}"

    response
  rescue IOError
    puts "Sunucu bağlantı hatası."
  rescue => e
    puts "Kapasite talep hatası: #{e.message}"
  end
end

# Python sunucusuna kapasite bilgisi gönderir
def forward_capacity_to_python(socket, capacity, server_id)
  begin
    updated_capacity = Capacity.new(
      server_id: server_id,
      serverX_status: capacity.serverX_status,
      timestamp: capacity.timestamp
    )
    socket.write([updated_capacity.to_proto.size].pack("N") + updated_capacity.to_proto)
    puts "Python sunucusuna kapasite bilgisi gönderildi."
  rescue IOError
    puts "Python bağlantı hatası."
  rescue => e
    puts "Python kapasite gönderim hatası: #{e.message}"
  end
end

# Sunucular arasında veri akışını yönetir
def manage_server_requests(java_connections, python_connection, fault_tolerance_level)
  responses = []
  java_connections.each_value do |connection|
    response = transmit_configuration(connection, fault_tolerance_level)
    responses << response if response
  end

  loop do
    java_connections.each_with_index do |(server_id, connection), index|
      if responses[index]&.response.to_s == "YEP"
        capacity = request_server_capacity(connection, server_id)
        forward_capacity_to_python(python_connection, capacity, server_id) if capacity
      else
        puts "Sunucu #{server_id} gerekli izinleri vermedi."
      end
    end
    sleep(5)
  end
end

# Bağlantıları sürekli kontrol ederek veri aktarımını sağlar
def monitor_connections(java_connections, python_connection, fault_tolerance_level)
  loop do
    if java_connections.any? && python_connection
      manage_server_requests(java_connections, python_connection, fault_tolerance_level)
    else
      puts "Bağlantılar başarısız, yeniden deneniyor..."
      sleep(5)
      python_connection = establish_python_connection
      java_connections = establish_java_connections
    end
  end
end

fault_tolerance_level = load_configuration("dist_subs.conf")
python_connection = establish_python_connection
java_connections = establish_java_connections

monitor_connections(java_connections, python_connection, fault_tolerance_level)