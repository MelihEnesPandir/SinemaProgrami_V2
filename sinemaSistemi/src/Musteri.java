public class Musteri {
    private int id;
    private String ad;
    private String email;
    private String telefonno;

    public Musteri(int id, String ad, String email, String telefonno) {
        this.id = id;
        this.ad = ad;
        this.email = email;
        this.telefonno = telefonno;
    }

    public int getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefonno() {
        return telefonno;
    }

    public void bilgiGoster() {
        System.out.println("Müşteri ID: " + id);
        System.out.println("Müşteri Adı: " + ad);
        System.out.println("Müşteri Email: " + email);
        System.out.println("Müşteri Telefon Numarası: " + telefonno);
    }
}
