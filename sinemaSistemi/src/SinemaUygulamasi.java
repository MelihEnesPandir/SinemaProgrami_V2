import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;

public class SinemaUygulamasi {

    private static List<Salon> salonlar = new ArrayList<>();
    private static List<Musteri> musteriler = new ArrayList<>();
    private static int musteriIdCounter = 100;

    public static void main(String[] args) {
        // Filmleri oluşturuyoruz
        Film film1 = new Film("Titanic", 195, "Romantik");
        Film film2 = new Film("Avatar", 162, "Bilim Kurgu");
        Film film3 = new Film("The Dark Knight", 152, "Aksiyon");
        Film film4 = new Film("Inception", 148, "Bilim Kurgu");
        Film film5 = new Film("Forrest Gump", 142, "Dram");
        Film film6 = new Film("Interstellar", 169, "Bilim Kurgu");
        Film film7 = new Film("The Godfather", 175, "Suç");
        Film film8 = new Film("The Matrix", 136, "Bilim Kurgu");
        Film film9 = new Film("The Shawshank Redemption", 142, "Dram");
        Film film10 = new Film("Pulp Fiction", 154, "Suç");

        // Salonları oluşturuyoruz
        Salon salon1 = new Salon(1, "Salon 1");
        Salon salon2 = new Salon(2, "Salon 2");
        Salon salon3 = new Salon(3, "Salon 3");
        Salon salon4 = new Salon(4, "Salon 4");
        Salon salon5 = new Salon(5, "Salon 5");

        // Film ve saatleri ekliyoruz
        salon1.filmEkle(film1, Arrays.asList("12:00", "16:00", "20:00"));
        salon1.filmEkle(film2, Arrays.asList("10:00", "14:00", "18:00"));

        salon2.filmEkle(film3, Arrays.asList("12:00", "16:00", "20:00"));
        salon2.filmEkle(film4, Arrays.asList("10:00", "14:00", "18:00"));

        salon3.filmEkle(film5, Arrays.asList("12:00", "16:00", "20:00"));
        salon3.filmEkle(film6, Arrays.asList("10:00", "14:00", "18:00"));

        salon4.filmEkle(film7, Arrays.asList("13:00", "17:00", "21:00"));
        salon4.filmEkle(film8, Arrays.asList("11:00", "15:00", "19:00"));

        salon5.filmEkle(film9, Arrays.asList("13:00", "17:00", "21:00"));
        salon5.filmEkle(film10, Arrays.asList("11:00", "15:00", "19:00"));

        // Salonları listeye ekliyoruz
        salonlar.add(salon1);
        salonlar.add(salon2);
        salonlar.add(salon3);
        salonlar.add(salon4);
        salonlar.add(salon5);

        // Kullanıcı etkileşimi başlatılıyor
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Yeni Müşteri Ekle");
            System.out.println("2. Salonları ve Gösterim Bilgilerini Görüntüle");
            System.out.println("3. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            int secim = scanner.nextInt();
            scanner.nextLine(); // buffer temizleme

            switch (secim) {
                case 1:
                    // Yeni müşteri ekleme
                    System.out.print("Müşteri Adı: ");
                    String ad = scanner.nextLine();
                    System.out.print("Müşteri Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Müşteri Telefon Numarası: ");
                    String telefonno = scanner.nextLine();
                    Musteri yeniMusteri = new Musteri(musteriIdCounter++, ad, email, telefonno);

                    System.out.println("Mevcut salonlar ve gösterim bilgileri:");
                    for (Salon salon : salonlar) {
                        salon.bilgiGoster();
                    }

                    System.out.print("Hangi salona kaydolmak istersiniz? (Salon ID): ");
                    int salonId = scanner.nextInt();
                    scanner.nextLine(); // buffer temizleme

                    Salon secilenSalon = salonlar.stream()
                            .filter(salon -> salon.getId() == salonId)
                            .findFirst()
                            .orElse(null);

                    if (secilenSalon != null) {
                        secilenSalon.kayitOl(yeniMusteri);
                    } else {
                        System.out.println("Geçersiz salon ID.");
                    }
                    kaydetBiletliMusterilerJson();
                    break;

                case 2:
                    // Salonları ve film bilgilerini görüntüle
                    for (Salon salon : salonlar) {
                        salon.bilgiGoster();
                    }
                    break;

                case 3:
                    // Çıkış
                    System.out.println("Çıkış yapılıyor...");
                    scanner.close();

                    // JSON dosyasına kaydetme işlemini burada çağırıyoruz
                    kaydetFilmlerJson();  // Filmleri JSON dosyasına kaydet
                    return;

                default:
                    System.out.println("Geçersiz seçim, tekrar deneyin.");
            }
        }
    }
    public static void kaydetBiletliMusterilerJson() {
        JsonArray musteriArray = new JsonArray();

        // Müşteri bilgilerini JSON formatına çevir
        for (Musteri musteri : musteriler) {
            JsonObject musteriObject = new JsonObject();
            musteriObject.addProperty("id", musteri.getId());
            musteriObject.addProperty("ad", musteri.getAd());
            musteriObject.addProperty("email", musteri.getEmail());
            musteriObject.addProperty("telefon", musteri.getTelefonno());
            musteriArray.add(musteriObject);
        }

        // JSON verisini dosyaya kaydet
        try (FileWriter writer = new FileWriter("musteriler.json")) {
            new Gson().toJson(musteriArray, writer);
            System.out.println("Müşteriler başarıyla musteriler.json dosyasına kaydedildi.");
        } catch (IOException e) {
            System.err.println("Dosyaya yazma sırasında bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void kaydetFilmlerJson() {
        JsonArray filmArray = new JsonArray();

        for (Salon salon : salonlar) {
            for (Map.Entry<Film, List<String>> entry : salon.getFilmVeSaatler().entrySet()) {
                JsonObject filmObject = new JsonObject();
                Film film = entry.getKey();
                List<String> saatler = entry.getValue();

                filmObject.addProperty("name", film.getName());
                filmObject.addProperty("duration", film.getDuration());
                filmObject.addProperty("genre", film.getGenre());

                JsonArray salonlarArray = new JsonArray();
                salonlarArray.add(salon.getName());

                filmObject.add("salonlar", salonlarArray);

                JsonArray saatlerArray = new JsonArray();
                for (String saat : saatler) {
                    saatlerArray.add(saat);
                }

                filmObject.add("saatler", saatlerArray);

                filmArray.add(filmObject);
            }
        }

        // Filmleri "Filmler.json" dosyasına kaydediyoruz
        try (FileWriter writer = new FileWriter("Filmler.json")) {
            writer.write(new Gson().toJson(filmArray));
            System.out.println("Filmler başarıyla Filmler.json dosyasına kaydedildi.");
        } catch (IOException e) {
            e.printStackTrace();  // Hata mesajlarını görmek için
        }
    }

    public static List<Salon> getSalonlar() {
        return salonlar;
    }
}
