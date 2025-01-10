
# Dağıtık Abonelik Sistemi

## Proje Tanımı
Bu proje, Java, Python ve Ruby kullanarak geliştirilen, dağıtık ve hata toleranslı bir abonelik sistemidir. Sistemde farklı bileşenler işbirliği yaparak veri paylaşımı ve yönetimi sağlar.

## İçerik
- [En Son Güncellemeler](#en-son-güncellemeler)
- [Eksikler](#eksikler)
- [1. ServerX.java](#1-serverxjava)
- [2. plotter.py](#2-plotterpy)
- [3. ClientX.java / MyClient.java](#3-clientxjava--myclientjava)
- [4. admin.rb](#4-adminrb)
- [5. Protobuf Tanımları](#5-protobuf-tanımları)
- [Mimari](#mimari)
- [Başlatma Adımları](#başlatma-adımları)
- [Ekip Üyeleri](#ekip-üyeleri)
- [Sunum Videosu](#sunum-videosu)

## En Son Güncellemeler
- Merkezi sistem kaldırıldı.
- Hata tolerans ayarlaması yapıldı.
- Protoların ve java protobuf kütüphanesinin dizin yapısı düzenlendi.
- Protolar düzenlenerek fazla portlar silindi (mimari çizimlerine bakabilirsiniz).

## Eksikler
- Java Server'ları eskisi gibi AdminHandler, ClientHandler ve DistributedServerHandler diye sınıflara bölünmedi. (Ödev teslim tarihine yetişmedi)
- Java Protobuf kütüphanesi Maven veya Gradle kullanmak yerine manuel olarak eklenmesi.
- Sadece SUBS ve DEL işlemleri var. Diğer işlemler yapılamıyor.
- Videoda sadece kodlar koşturuldu. Meet görüşmesi yapamadık.

---

## 1. ServerX.java
**Özellikler:**
- TCP soketleri ile bağlantı.
- `SUBS` ve `DEL` taleplerini işleme.
- Protobuf formatında veri iletimi.
- Thread-safe yapı.
- Admin'den gelen komutlarla hata toleransı yönetimi.
- Kapasite bilgisi gönderimi.
- Hata tolerans seviyesine göre yedekleme yapar.

## 2. plotter.py
**Özellikler:**
- Gerçek zamanlı kapasite takibi.
- Protobuf formatında veri iletimi.
- Dinamik çizgi grafikleri.
- Threading.

## 3. ClientX.java / MyClient.java
**Özellikler:**
- TCP bağlantısı.
- Protobuf formatında veri iletimi.
- Kullanıcı etkileşimiyle ID ve talep türü seçimi.

## 4. admin.rb
**Özellikler:**
- `dist_subs.conf` üzerinden hata tolerans seviyesini okur.
- Protobuf formatında veri iletimi.
- Java ve Python sunucularına bağlantı kurar.
- `STRT` komutunu göndererek sunucuları başlatır.
- Kapasite takibi ve aktarımı.

**Genel Akış:**
1. Konfigürasyon okunur.
2. Bağlantılar kurulur.
3. Sunucular başlatılır.
4. Kapasite sorgulanır ve gönderilir.
5. Kapasite bilgileri çizdirilir.

## 5. Protobuf Tanımları

### Message.proto
- **Message:** `demand` ve `response` bilgilerini taşır.
- **Demand Enum:** `CPCTY` (kapasite sorgusu), `STRT` (başlatma komutu).
- **Response Enum:** `YEP` (başarılı), `NOP` (başarısız).

### Subscriber.proto
- **Subscriber:** Kullanıcı bilgilerini ve taleplerini içerir.
- **DemandType Enum:** `SUBS`, `DEL`, `UPDT`, `ONLN`, `OFFL`.

### Capacity.proto
- **Capacity:** Sunucu kapasite durumu ve zaman bilgisi.

### Configuration.proto
- **Configuration:** Hata tolerans seviyesi ve işlem türü.
- **MethodType Enum:** `STRT`, `STOP`.

---

## Mimari
<img src="Mimariler/yeni_mimari.png.png" alt="" width="400"/>

---


**Başlatma Adımları:**
1. Sunucuları başlatın.
2. Admin panelini çalıştırın.
3. İstemciler üzerinden işlem gerçekleştirin.

---

**Ekip Üyeleri:**
1. Aykhan Shirinzade
2. Beyzanur Dere
3. Pınar Güzel
4. Elgun Heydarov

---

## Sunum Videosu
