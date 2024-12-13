# Dağıtık Abonelik Sistemi (Distributed Subscriber Service)
Bu belge, projenin işlevselliğini ve kullanılan dosyaların detaylarını açıklamaktadır.

## Derleme ve Çalıştırma Aşamaları

### Protobuf Derleme

- **dist_servers** klasöründe:

  ```bash
  protoc --java_out=. <Proto Dosya İsmi>.proto
  ```

- **panel** klasöründe:

  ```bash
  protoc --ruby_out=. <Proto Dosya İsmi>.proto
  ```

### Java Dosyalarını Derleme

- **dist_servers** klasöründe:

  ```bash
  javac -cp ".;com/google/protobuf/protobuf-java-4.28.3.jar" *.java
  ```

### Java Dosyalarını Çalıştırma

- **dist_servers** klasöründe:

  ```bash
  java -cp ".;com/google/protobuf/protobuf-java-4.28.3.jar" <Server adı>
  ```

### Ruby Admin Panelini Çalıştırma

- **panel** klasöründe:

  ```bash
  ruby admin.rb
  ```

### Java Client Çalıştırma

- **Clients** klasöründe:

  ```bash
  javac -cp ".;com/google/protobuf/protobuf-java-4.28.3.jar" <Client adı>.java
  java -cp ".;com/google/protobuf/protobuf-java-4.28.3.jar" <Client adı>
  ```

### Çalıştırma Sıralaması

1. plotter.py dosyasını çalıştırın.
2. **Admin Panelini** (admin.rb) çalıştırın. Admin paneli, sunuculara bağlanmaya çalışır ve bağlanana kadar düzenli aralıklarla bağlantı isteği gönderir.
3. **Java Server** dosyalarını başlatın.
4. İstemcilerle bağlantı kurmak için **Client** dosyalarını çalıştırın.

## Dosyaların Açıklamaları

### Server Dosyaları (Server1, Server2, Server3)

- **Amaç:** Sunucular, Subscriber istemcilerden gelen talepleri işlemek, Admin istemcisinden gelen yönetim taleplerini yerine getirmek ve diğer sunucularla iletişim kurarak dağıtık bir sistem oluşturmak için geliştirilmiştir.
- **Bağlantı Yönetimi:** 
  - Admin istemcilerle iletişim için portlar: 6001, 6002, 6003.
  - Subscriber istemciler ve diğer sunucularla iletişim için ayrı portlar kullanılır.
- **İşlevler:** 
  - Admin Komutları:
    - `STRT`: Sunucuyu başlatır.
    - `CPCTY`: Kapasite durumunu döner.
  - Subscriber Komutları:
    - `SUBS`: Abone ekler.
    - `DEL`: Abone siler.
- **Protobuf Kullanımı:** 
  - `Message.proto`: Admin komutları ve yanıtları için kullanılır.
  - `Subscriber.proto`: Subscriber taleplerini işler.
  - `Capacity.proto`: Kapasite bilgilerini döner.

### DistributedServerHandler.java

- **Amaç:** Sunucular arasında bağlantı kurmayı ve bu bağlantıları yönetmeyi sağlar.
- **İşlevler:** 
  - Her sunucu, diğer iki sunucuya TCP bağlantısı kurar.
  - Bağlantılar kesildiğinde, yeniden bağlanmayı dener.
- **Teknik Özellikler:** 
  - Bağlantılar sürekli kontrol edilir ve açık tutulur.
  - Sunucular arasında 6 bağlantı kurulur.

### ClientHandler.java

- **Amaç:** Subscriber istemcilerden gelen talepleri işler.
- **İşlevler:** 
  - `SUBS`: Yeni abone ekler.
  - `DEL`: Abonelik iptali taleplerini işler.
  - Subscriber istemcilere geri bildirim mesajı gönderir.

### AdminHandler.java

- **Amaç:** Admin istemcilerden gelen komutları işler.
- **İşlevler:** 
  - `STRT`: Sunucuyu başlatır.
  - `CPCTY`: Kapasite bilgilerini döner.
- **Teknik Detaylar:** 
  - Admin istemcisi, sunucuların kapasite bilgilerini düzenli aralıklarla sorgular.

### Client.java

- **Amaç:** Subscriber istemciler için sunucuya bağlanma, abone olma ve abonelik iptali işlemlerini sağlar.
- **İşlevler:** 
  - `SUBS`: Abonelik talebi gönderir.
  - `DEL`: Abonelik iptal talebi gönderir.
- **Protobuf Kullanımı:** 
  - Talepler, `Subscriber.proto` kullanılarak Protobuf formatında gönderilir.

### Admin.rb

- **Amaç:** Admin istemcisi olarak, sunuculara yönetim komutları göndermek ve yanıtları işlemek için geliştirilmiştir.
- **İşlevler:** 
  - `STRT`: Tüm sunucuları başlatır.
  - `CPCTY`: Kapasite bilgilerini sorgular ve Plotter sunucusuna gönderir.
- **Teknik Özellikler:** 
  - Bağlantı Yenileme: Admin.rb, sunuculara bağlanmak için düzenli aralıklarla deneme yapar.
  - Kapasite Bilgileri: Sunuculardan gelen kapasite bilgilerini işler.

## Proto Dosyaları

### Capacity.proto

- **Amaç:** Kapasite bilgilerini taşır.
- **Alanlar:** 
  - `serverXStatus`: Sunucunun kapasite durumu (%).
  - `timestamp`: Kapasite bilgisinin alındığı UNIX zaman damgası.

### Configuration.proto

- **Amaç:** Sunucu konfigürasyon ayarlarını taşır.
- **Alanlar:** 
  - `fault_tolerance_level`: Hata tolerans seviyesini belirtir.
  - `method`: Yönetim yöntemi.

### Message.proto

- **Amaç:** Admin komutlarını taşır.
- **Alanlar:** 
  - `demand`: Komut türü.
  - `response`: Sunucunun yanıt durumu.

### Subscriber.proto

- **Amaç:** Subscriber istemci taleplerini tanımlar.
- **Alanlar:** 
  - `ID`: Abone kimlik numarası.
  - `name_surname`: Abonenin adı.
  - `start_date`: Kayıt tarihi.
  - `last_accessed`: Son erişim tarihi.
  - `interests`: İlgi alanları.
  - `isOnline`: Çevrimiçi durumu.

## Eksikler ve Hatalar

### Plotter.py
- Python tabanlı sunucu olan Plotter, Admin istemcisinden kapasite bilgilerini alarak grafik oluşturur. Ancak, yanıt iletimiyle ilgili sorunlar nedeniyle entegrasyonu tamamlanmamıştır.

