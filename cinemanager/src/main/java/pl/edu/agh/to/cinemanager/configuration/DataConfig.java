package pl.edu.agh.to.cinemanager.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.agh.to.cinemanager.model.*;
import pl.edu.agh.to.cinemanager.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataConfig {
    private void calculateTotalPrice(Order order, List<Ticket> tickets) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Ticket ticket : tickets) {
            BigDecimal price = ticket.isDiscounted() ? ticket.getScreening().getScreeningType().getDiscountPrice() : ticket.getScreening().getScreeningType().getBasePrice();
            totalPrice = totalPrice.add(price);
        }
        order.setTotalPrice(totalPrice);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                        MovieRepository movieRepository, GenreRepository genreRepository,
                                        ScreeningTypeRepository screeningTypeRepository,
                                        ScreeningRepository screeningRepository,
                                        CinemaRoomRepository cinemaRoomRepository, OrderRepository orderRepository, TicketRepository ticketRepository, DirectorRepository directorRepository, ReviewRepository reviewRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User user1 = new User("Jan", "Kowalski", "jan@mail.com",
                        passwordEncoder.encode("password"), UserRole.ADMINISTRATOR);
                User user2 = new User("Kamil", "Slimak", "kamil@snail.com",
                        passwordEncoder.encode("password"), UserRole.CUSTOMER);
                userRepository.save(user1);
                userRepository.save(user2);

                Genre action = new Genre("Akcja");
                Genre drama = new Genre("Dramat");
                Genre comedy = new Genre("Komedia");
                Genre thriller = new Genre("Thriller");
                Genre fantasy = new Genre("Fantasy");
                Genre horror = new Genre("Horror");
                Genre sciFi = new Genre("Science Fiction");
                Genre animation = new Genre("Animacja");
                Genre romance = new Genre("Romans");
                Genre mystery = new Genre("Kryminalne");

                genreRepository.saveAll(List.of(action, drama, comedy, thriller, fantasy,
                        horror, sciFi, animation, romance, mystery));

                Director frankDarabont = new Director("Frank", "Darabont");
                Director lanaWachowski = new Director("Lana", "Wachowski");
                Director christopherNolan = new Director("Christopher", "Nolan");
                Director robertZemeckis = new Director("Robert", "Zemeckis");
                Director andrewAdamson = new Director("Andrew", "Adamson");
                Director quentinTarantino = new Director("Quentin", "Tarantino");
                Director jamesCameron = new Director("James", "Cameron");
                Director ridleyScott = new Director("Ridley", "Scott");
                Director toddPhillips = new Director("Todd", "Phillips");
                Director johnLasseter = new Director("John", "Lasseter");
                Director brianDePalma = new Director("Brian", "De Palma");
                Director zackSnyder = new Director("Zack", "Snyder");
                Director goreVerbinski = new Director("Gore", "Verbinski");
                Director guyRitchie = new Director("Guy", "Ritchie");
                Director chrisBuck = new Director("Chris", "Buck");
                Director peterJackson = new Director("Peter", "Jackson");

                directorRepository.saveAll(List.of(
                        frankDarabont, lanaWachowski, christopherNolan, robertZemeckis, andrewAdamson,
                        quentinTarantino, jamesCameron, ridleyScott, toddPhillips, johnLasseter,
                        brianDePalma, zackSnyder, goreVerbinski, guyRitchie, chrisBuck, peterJackson
                ));


                Movie movie1 = new Movie(
                        "Władca Pierścieni: Drużyna Pierścienia",
                        "Grupa bohaterów wyrusza w podróż, aby zniszczyć pierścień władzy.",
                        peterJackson,
                        "posters/wladca_pierscieni_druzyna_pierscienia.webp",
                        178,
                        fantasy
                );

                Movie movie2 = new Movie(
                        "Skazani na Shawshank",
                        "Młody bankier niesłusznie skazany na dożywocie odnajduje nadzieję za kratami.",
                        frankDarabont,
                        "posters/skazani_na_shawshank.jpg",
                        142,
                        drama
                );

                Movie movie3 = new Movie(
                        "Matrix",
                        "Haker odkrywa prawdę o rzeczywistości i bierze udział w walce przeciwko maszynom.",
                        lanaWachowski,
                        "posters/matrix.jpg",
                        136,
                        sciFi
                );

                Movie movie4 = new Movie(
                        "Incepcja",
                        "Zespół specjalistów kradnie informacje, włamując się do ludzkich snów.",
                        christopherNolan,
                        "posters/incepcja.jpg",
                        148,
                        sciFi
                );

                Movie movie5 = new Movie(
                        "Forrest Gump",
                        "Historia życia prostego człowieka, który przypadkowo staje się świadkiem wielkich wydarzeń.",
                        robertZemeckis,
                        "posters/forrest_gump.jpg",
                        142,
                        drama
                );

                Movie movie6 = new Movie(
                        "Shrek",
                        "Ogr wyrusza na misję, aby uratować księżniczkę, i odkrywa przyjaźń po drodze.",
                        andrewAdamson,
                        "posters/shrek.jpg",
                        90,
                        animation
                );

                Movie movie7 = new Movie(
                        "Pulp Fiction",
                        "Splątane historie kryminalne w Los Angeles.",
                        quentinTarantino,
                        "posters/pulp_fiction.jpg",
                        154,
                        thriller
                );

                Movie movie8 = new Movie(
                        "Titanic",
                        "Historia miłosna na tle katastrofy RMS Titanic.",
                        jamesCameron,
                        "posters/titanic.jpg",
                        195,
                        romance
                );

                Movie movie9 = new Movie(
                        "Gladiator",
                        "Były generał rzymski zostaje gladiatorem, aby zemścić się na cesarzu.",
                        ridleyScott,
                        "posters/gladiator.jpg",
                        155,
                        action
                );

                Movie movie10 = new Movie(
                        "Joker",
                        "Historia psychologiczna przemiany Arthura Flecka w legendarnego przestępcę.",
                        toddPhillips,
                        "posters/joker.jpg",
                        122,
                        drama
                );

                Movie movie11 = new Movie(
                        "Interstellar",
                        "Zespół naukowców podróżuje przez czarną dziurę, aby znaleźć nowy dom dla ludzkości.",
                        christopherNolan,
                        "posters/interstellar.jpg",
                        169,
                        sciFi
                );

                Movie movie12 = new Movie(
                        "Obcy: Ósmy pasażer Nostromo",
                        "Załoga statku kosmicznego walczy z przerażającym obcym stworzeniem.",
                        ridleyScott,
                        "posters/obcy_osmy_pasazer_nostromo.jpg",
                        117,
                        horror
                );

                Movie movie13 = new Movie(
                        "Toy Story",
                        "Zabawki ożywają, gdy ludzie nie patrzą, i przeżywają niesamowite przygody.",
                        johnLasseter,
                        "posters/toy_story.jpg",
                        81,
                        animation
                );

                Movie movie14 = new Movie(
                        "Zielona Mila",
                        "Strażnik więzienny zaprzyjaźnia się z więźniem posiadającym nadnaturalne moce.",
                        frankDarabont,
                        "posters/zielona_mila.jpg",
                        189,
                        drama
                );

                Movie movie15 = new Movie(
                        "Mission: Impossible",
                        "Agent Ethan Hunt musi oczyścić swoje imię i powstrzymać spisek.",
                        brianDePalma,
                        "posters/mission_impossible.jpg",
                        110,
                        action
                );

                Movie movie16 = new Movie(
                        "Człowiek ze Stali",
                        "Młody Clark Kent odkrywa swoje moce i staje się Supermanem.",
                        zackSnyder,
                        "posters/czlowiek_ze_stali.jpg",
                        143,
                        sciFi
                );

                Movie movie17 = new Movie(
                        "Piraci z Karaibów: Klątwa Czarnej Perły",
                        "Piracka przygoda z Jackiem Sparrowem i tajemniczą klątwą.",
                        goreVerbinski,
                        "posters/piraci_z_karaibow_klatwa_czarnej_perly.jpg",
                        143,
                        fantasy
                );

                Movie movie18 = new Movie(
                        "Sherlock Holmes",
                        "Słynny detektyw i jego przyjaciel Watson rozwiązują zagadki kryminalne.",
                        guyRitchie,
                        "posters/sherlock_holmes.webp",
                        128,
                        mystery
                );

                Movie movie19 = new Movie(
                        "Kraina Lodu",
                        "Księżniczki Anna i Elsa wyruszają w przygodę w świecie pełnym lodu i magii.",
                        chrisBuck,
                        "posters/kraina_lodu.jpg",
                        102,
                        animation
                );

                Movie movie20 = new Movie(
                        "Avatar",
                        "Były żołnierz odkrywa magię planety Pandora.",
                        jamesCameron,
                        "posters/avatar.jpg",
                        162,
                        sciFi
                );

                movieRepository.saveAll(List.of(
                        movie1, movie2, movie3, movie4, movie5,
                        movie6, movie7, movie8, movie9, movie10,
                        movie11, movie12, movie13, movie14, movie15,
                        movie16, movie17, movie18, movie19, movie20
                ));

                ScreeningType type2D = new ScreeningType("2D", new BigDecimal("20.00"), new BigDecimal("15.00"));
                ScreeningType type3D = new ScreeningType("3D", new BigDecimal("25.00"), new BigDecimal("20.00"));

                screeningTypeRepository.saveAll(List.of(type2D, type3D));

                CinemaRoom roomShire = new CinemaRoom("Shire", 10, 20);
                CinemaRoom roomMordor = new CinemaRoom("Mordor", 15, 25);
                CinemaRoom roomMatrix = new CinemaRoom("Matrix", 12, 22);
                CinemaRoom roomTitanic = new CinemaRoom("Titanic", 8, 18);
                CinemaRoom roomPandora = new CinemaRoom("Pandora", 14, 30);

                cinemaRoomRepository.saveAll(List.of(roomShire, roomMordor, roomMatrix, roomTitanic, roomPandora));

                List<Screening> screenings = List.of(
                        // Week 1-2 (01.12.2024 - 14.12.2024) - Movies 1, 2, 3, 4, 5
                        new Screening(LocalDateTime.of(2024, 12, 1, 12, 0), movie1, roomShire, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 1, 15, 0), movie2, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 2, 18, 0), movie3, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 3, 12, 0), movie4, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 4, 15, 0), movie5, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 5, 15, 0), movie2, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 6, 12, 0), movie1, roomShire, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 6, 18, 0), movie3, roomMatrix, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 6, 21, 0), movie3, roomMatrix, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 7, 15, 0), movie2, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 7, 15, 0), movie4, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 8, 15, 0), movie5, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 9, 12, 0), movie4, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 10, 15, 0), movie5, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 10, 12, 0), movie1, roomShire, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 11, 15, 0), movie2, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 11, 15, 0), movie3, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 12, 12, 0), movie1, roomShire, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 13, 12, 0), movie4, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 13, 18, 0), movie3, roomMatrix, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 14, 15, 0), movie5, roomPandora, type3D),


                        // Week 3-4 (15.12.2024 - 28.12.2024) - Movies 4, 5, 6, 7, 8
                        new Screening(LocalDateTime.of(2024, 12, 15, 12, 0), movie4, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 15, 15, 0), movie6, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 16, 18, 0), movie7, roomShire, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 17, 12, 0), movie8, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 18, 15, 0), movie5, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 20, 12, 0), movie6, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 22, 15, 0), movie7, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 24, 12, 0), movie4, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 25, 15, 0), movie6, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 25, 18, 0), movie7, roomShire, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 26, 12, 0), movie8, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 27, 15, 0), movie5, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 27, 12, 0), movie6, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 28, 15, 0), movie7, roomMatrix, type2D),

                        // Week 5-6 (29.12.2024 - 11.01.2025) - Movies 9, 10, 11, 12, 1
                        new Screening(LocalDateTime.of(2024, 12, 29, 12, 0), movie9, roomShire, type2D),
                        new Screening(LocalDateTime.of(2024, 12, 29, 15, 0), movie10, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2024, 12, 30, 18, 0), movie11, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 1, 12, 0), movie12, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 2, 15, 0), movie1, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 3, 18, 0), movie10, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 3, 12, 0), movie9, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 4, 12, 0), movie12, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 4, 15, 0), movie1, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 5, 18, 0), movie10, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 5, 18, 0), movie12, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 5, 18, 0), movie9, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 6, 12, 0), movie9, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 7, 12, 0), movie12, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 7, 15, 0), movie1, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 8, 18, 0), movie10, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 9, 12, 0), movie9, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 10, 15, 0), movie1, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 11, 18, 0), movie10, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 11, 12, 0), movie9, roomShire, type3D),

                        // Week 7-8 (12.01.2025 - 25.01.2025) - Movies 13, 14, 15, 16, 17
                        new Screening(LocalDateTime.of(2025, 1, 12, 12, 0), movie13, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 12, 12, 0), movie15, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 13, 15, 0), movie14, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 13, 18, 0), movie17, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 13, 15, 0), movie16, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 14, 18, 0), movie15, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 15, 12, 0), movie16, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 15, 14, 0), movie13, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 16, 15, 0), movie14, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 16, 15, 0), movie17, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 17, 12, 0), movie13, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 17, 12, 0), movie15, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 18, 15, 0), movie14, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 18, 18, 0), movie17, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 18, 15, 0), movie16, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 20, 18, 0), movie15, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 21, 12, 0), movie16, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 21, 14, 0), movie13, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 22, 15, 0), movie14, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 23, 15, 0), movie17, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 24, 15, 0), movie14, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 24, 15, 0), movie17, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 25, 12, 0), movie13, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 25, 12, 0), movie15, roomMatrix, type2D),


                        // Week 9 (26.01.2025 - 01.02.2025) - Movies 18, 19, 20
                        new Screening(LocalDateTime.of(2025, 1, 26, 12, 0), movie18, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 26, 12, 0), movie19, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 26, 15, 0), movie20, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 26, 18, 0), movie18, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 26, 20, 0), movie19, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 27, 12, 0), movie20, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 27, 15, 0), movie19, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 27, 15, 0), movie18, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 27, 18, 0), movie20, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 27, 20, 0), movie18, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 28, 12, 0), movie19, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 28, 15, 0), movie18, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 28, 15, 0), movie20, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 28, 18, 0), movie19, roomMatrix, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 28, 20, 0), movie20, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 29, 12, 0), movie18, roomShire, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 29, 15, 0), movie19, roomMordor, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 29, 18, 0), movie20, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 29, 18, 0), movie18, roomTitanic, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 29, 20, 0), movie19, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 30, 12, 0), movie18, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 30, 12, 0), movie19, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 30, 15, 0), movie20, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 30, 18, 0), movie18, roomMatrix, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 30, 20, 0), movie19, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 31, 12, 0), movie19, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 31, 15, 0), movie19, roomPandora, type3D),
                        new Screening(LocalDateTime.of(2025, 1, 31, 18, 0), movie20, roomTitanic, type2D),
                        new Screening(LocalDateTime.of(2025, 1, 31, 20, 0), movie18, roomMatrix, type2D),
                        new Screening(LocalDateTime.of(2025, 2, 1, 12, 0), movie20, roomShire, type3D),
                        new Screening(LocalDateTime.of(2025, 2, 1, 15, 0), movie19, roomMordor, type2D),
                        new Screening(LocalDateTime.of(2025, 2, 1, 18, 0), movie18, roomPandora, type2D),
                        new Screening(LocalDateTime.of(2025, 2, 1, 20, 0), movie20, roomTitanic, type3D)
                );

                screeningRepository.saveAll(screenings);

                User user3 = new User("Jan", "Kowalski", "user3@mail.com", passwordEncoder.encode("password3"), UserRole.CUSTOMER);
                User user4 = new User("Anna", "Nowak", "user4@mail.com", passwordEncoder.encode("password4"), UserRole.CUSTOMER);
                User user5 = new User("Piotr", "Wiśniewski", "user5@mail.com", passwordEncoder.encode("password5"), UserRole.CUSTOMER);
                User user6 = new User("Katarzyna", "Wójcik", "user6@mail.com", passwordEncoder.encode("password6"), UserRole.CUSTOMER);
                User user7 = new User("Tomasz", "Kowalczyk", "user7@mail.com", passwordEncoder.encode("password7"), UserRole.CUSTOMER);
                User user8 = new User("Agnieszka", "Kamińska", "user8@mail.com", passwordEncoder.encode("password8"), UserRole.CUSTOMER);
                User user9 = new User("Marek", "Lewandowski", "user9@mail.com", passwordEncoder.encode("password9"), UserRole.CUSTOMER);
                User user10 = new User("Ewa", "Zielińska", "user10@mail.com", passwordEncoder.encode("password10"), UserRole.CUSTOMER);
                User user11 = new User("Paweł", "Szymański", "user11@mail.com", passwordEncoder.encode("password11"), UserRole.CUSTOMER);
                User user12 = new User("Magdalena", "Woźniak", "user12@mail.com", passwordEncoder.encode("password12"), UserRole.CUSTOMER);
                userRepository.saveAll(List.of(user3, user4, user5, user6, user7, user8, user9, user10, user11, user12));

                User manager = new User("Iwona", "Dąbrowska", "manager@mail.com", passwordEncoder.encode("password"), UserRole.MANAGER);
                User employee = new User("Zbigniew", "Malanowski", "employee@mail.com", passwordEncoder.encode("password"), UserRole.EMPLOYEE);
                userRepository.saveAll(List.of(manager, employee));

                Order order1 = new Order(LocalDateTime.of(2024, 11, 15, 10, 0), BigDecimal.ZERO, user3);
                Order order2 = new Order(LocalDateTime.of(2024, 11, 16, 11, 0), BigDecimal.ZERO, user4);
                Order order3 = new Order(LocalDateTime.of(2024, 11, 17, 12, 0), BigDecimal.ZERO, user5);
                Order order4 = new Order(LocalDateTime.of(2024, 11, 18, 13, 0), BigDecimal.ZERO, user6);
                Order order5 = new Order(LocalDateTime.of(2024, 11, 19, 14, 0), BigDecimal.ZERO, user7);
                Order order6 = new Order(LocalDateTime.of(2024, 11, 20, 15, 0), BigDecimal.ZERO, user8);
                Order order7 = new Order(LocalDateTime.of(2024, 11, 21, 16, 0), BigDecimal.ZERO, user9);
                Order order8 = new Order(LocalDateTime.of(2024, 11, 22, 17, 0), BigDecimal.ZERO, user10);
                Order order9 = new Order(LocalDateTime.of(2024, 11, 23, 18, 0), BigDecimal.ZERO, user11);
                Order order10 = new Order(LocalDateTime.of(2024, 11, 24, 19, 0), BigDecimal.ZERO, user12);
                Order order11 = new Order(LocalDateTime.of(2024, 12, 24, 20, 0), BigDecimal.ZERO, user3);
                Order order12 = new Order(LocalDateTime.of(2024, 12, 25, 21, 0), BigDecimal.ZERO, user4);
                Order order13 = new Order(LocalDateTime.of(2024, 12, 26, 22, 0), BigDecimal.ZERO, user5);
                Order order14 = new Order(LocalDateTime.of(2024, 12, 27, 23, 0), BigDecimal.ZERO, user6);
                Order order15 = new Order(LocalDateTime.of(2024, 12, 28, 10, 0), BigDecimal.ZERO, user7);
                Order order16 = new Order(LocalDateTime.of(2024, 12, 29, 11, 0), BigDecimal.ZERO, user8);
                Order order17 = new Order(LocalDateTime.of(2024, 12, 30, 12, 0), BigDecimal.ZERO, user9);
                Order order18 = new Order(LocalDateTime.of(2024, 12, 31, 13, 0), BigDecimal.ZERO, user10);
                Order order19 = new Order(LocalDateTime.of(2025, 1, 1, 14, 0), BigDecimal.ZERO, user11);
                Order order20 = new Order(LocalDateTime.of(2025, 1, 2, 15, 0), BigDecimal.ZERO, user12);

                order1.setCancelled(true);
                order2.setPaid(true);
                order3.setPaid(true);
                order4.setPaid(true);
                order5.setPaid(true);
                order6.setCancelled(true);
                order7.setPaid(true);
                order8.setPaid(true);
                order9.setPaid(true);
                order10.setPaid(true);
                order11.setPaid(true);
                order12.setPaid(true);
                order13.setPaid(true);
                order14.setPaid(true);
                order15.setPaid(true);
                order16.setPaid(true);
                order17.setPaid(true);
                order18.setPaid(true);
                order19.setPaid(true);
                order20.setCancelled(true);
                order20.setPaid(true);

                List<Ticket> tickets1 = List.of(
                        new Ticket(6, 13, screenings.get(0), user3, order1, true),
                        new Ticket(5, 2, screenings.get(0), user3, order1, false),
                        new Ticket(5, 9, screenings.get(0), user3, order1, false),
                        new Ticket(4, 3, screenings.get(0), user3, order1, true),
                        new Ticket(8, 20, screenings.get(0), user3, order1, true),
                        new Ticket(6, 10, screenings.get(0), user3, order1, true),
                        new Ticket(6, 14, screenings.get(0), user3, order1, true),
                        new Ticket(7, 13, screenings.get(0), user3, order1, true),
                        new Ticket(1, 4, screenings.get(0), user3, order1, false),
                        new Ticket(6, 10, screenings.get(0), user3, order1, false),
                        new Ticket(4, 4, screenings.get(0), user3, order1, true),
                        new Ticket(1, 10, screenings.get(0), user3, order1, false),
                        new Ticket(4, 3, screenings.get(0), user3, order1, true),
                        new Ticket(4, 9, screenings.get(0), user3, order1, true),
                        new Ticket(2, 10, screenings.get(0), user3, order1, true),
                        new Ticket(1, 6, screenings.get(0), user3, order1, true),
                        new Ticket(5, 18, screenings.get(0), user3, order1, false),
                        new Ticket(4, 14, screenings.get(0), user3, order1, true),
                        new Ticket(2, 10, screenings.get(0), user3, order1, false),
                        new Ticket(8, 12, screenings.get(0), user3, order1, true),
                        new Ticket(4, 10, screenings.get(0), user3, order1, false),
                        new Ticket(2, 10, screenings.get(0), user3, order1, false),
                        new Ticket(6, 14, screenings.get(0), user3, order1, false),
                        new Ticket(6, 11, screenings.get(0), user3, order1, false),
                        new Ticket(4, 7, screenings.get(0), user3, order1, true),
                        new Ticket(5, 16, screenings.get(0), user3, order1, false),
                        new Ticket(2, 1, screenings.get(0), user3, order1, false),
                        new Ticket(5, 18, screenings.get(0), user3, order1, false),
                        new Ticket(6, 11, screenings.get(0), user3, order1, false),
                        new Ticket(5, 7, screenings.get(0), user3, order1, true),
                        new Ticket(6, 12, screenings.get(0), user3, order1, true),
                        new Ticket(8, 9, screenings.get(0), user3, order1, true),
                        new Ticket(3, 2, screenings.get(0), user3, order1, true),
                        new Ticket(6, 13, screenings.get(0), user3, order1, true),
                        new Ticket(6, 8, screenings.get(0), user3, order1, false),
                        new Ticket(5, 20, screenings.get(0), user3, order1, false),
                        new Ticket(2, 13, screenings.get(0), user3, order1, false),
                        new Ticket(6, 7, screenings.get(0), user3, order1, false),
                        new Ticket(3, 9, screenings.get(0), user3, order1, true),
                        new Ticket(5, 12, screenings.get(0), user3, order1, true),
                        new Ticket(8, 7, screenings.get(0), user3, order1, false),
                        new Ticket(8, 6, screenings.get(0), user3, order1, true),
                        new Ticket(1, 4, screenings.get(0), user3, order1, true),
                        new Ticket(4, 12, screenings.get(0), user3, order1, false),
                        new Ticket(4, 12, screenings.get(0), user3, order1, false),
                        new Ticket(5, 11, screenings.get(0), user3, order1, false),
                        new Ticket(3, 20, screenings.get(0), user3, order1, true),
                        new Ticket(8, 10, screenings.get(0), user3, order1, true),
                        new Ticket(6, 4, screenings.get(0), user3, order1, false),
                        new Ticket(6, 7, screenings.get(0), user3, order1, true),
                        new Ticket(5, 16, screenings.get(0), user3, order1, true),
                        new Ticket(1, 8, screenings.get(0), user3, order1, true),
                        new Ticket(8, 7, screenings.get(0), user3, order1, true),
                        new Ticket(1, 1, screenings.get(0), user3, order1, true),
                        new Ticket(1, 8, screenings.get(0), user3, order1, false),
                        new Ticket(2, 9, screenings.get(0), user3, order1, false),
                        new Ticket(5, 14, screenings.get(0), user3, order1, true),
                        new Ticket(5, 8, screenings.get(0), user3, order1, false),
                        new Ticket(8, 10, screenings.get(0), user3, order1, true),
                        new Ticket(2, 6, screenings.get(0), user3, order1, false),
                        new Ticket(8, 14, screenings.get(0), user3, order1, true),
                        new Ticket(7, 6, screenings.get(0), user3, order1, true),
                        new Ticket(4, 2, screenings.get(0), user3, order1, false),
                        new Ticket(2, 2, screenings.get(0), user3, order1, true),
                        new Ticket(5, 9, screenings.get(0), user3, order1, false),
                        new Ticket(2, 6, screenings.get(0), user3, order1, true)
                );
                List<Ticket> tickets2 = List.of(
                        new Ticket(7, 14, screenings.get(1), user4, order2, true),
                        new Ticket(3, 12, screenings.get(1), user4, order2, false),
                        new Ticket(7, 13, screenings.get(1), user4, order2, true),
                        new Ticket(7, 16, screenings.get(1), user4, order2, false),
                        new Ticket(5, 14, screenings.get(1), user4, order2, true),
                        new Ticket(6, 16, screenings.get(1), user4, order2, true),
                        new Ticket(8, 17, screenings.get(1), user4, order2, true),
                        new Ticket(10, 13, screenings.get(1), user4, order2, true),
                        new Ticket(10, 15, screenings.get(1), user4, order2, true),
                        new Ticket(9, 15, screenings.get(1), user4, order2, true),
                        new Ticket(8, 16, screenings.get(1), user4, order2, false),
                        new Ticket(5, 3, screenings.get(1), user4, order2, true),
                        new Ticket(6, 14, screenings.get(1), user4, order2, false),
                        new Ticket(7, 17, screenings.get(1), user4, order2, false),
                        new Ticket(9, 17, screenings.get(1), user4, order2, false),
                        new Ticket(3, 15, screenings.get(1), user4, order2, false),
                        new Ticket(10, 14, screenings.get(1), user4, order2, true),
                        new Ticket(9, 16, screenings.get(1), user4, order2, false),
                        new Ticket(9, 13, screenings.get(1), user4, order2, true),
                        new Ticket(6, 9, screenings.get(1), user4, order2, false),
                        new Ticket(7, 4, screenings.get(1), user4, order2, true),
                        new Ticket(6, 15, screenings.get(1), user4, order2, true),
                        new Ticket(7, 16, screenings.get(1), user4, order2, false),
                        new Ticket(9, 13, screenings.get(1), user4, order2, true),
                        new Ticket(7, 14, screenings.get(1), user4, order2, true),
                        new Ticket(4, 12, screenings.get(1), user4, order2, false),
                        new Ticket(10, 12, screenings.get(1), user4, order2, true),
                        new Ticket(11, 1, screenings.get(1), user4, order2, false),
                        new Ticket(7, 15, screenings.get(1), user4, order2, true),
                        new Ticket(9, 11, screenings.get(1), user4, order2, false),
                        new Ticket(9, 10, screenings.get(1), user4, order2, true),
                        new Ticket(7, 10, screenings.get(1), user4, order2, true),
                        new Ticket(9, 19, screenings.get(1), user4, order2, true),
                        new Ticket(8, 20, screenings.get(1), user4, order2, false),
                        new Ticket(4, 6, screenings.get(1), user4, order2, true),
                        new Ticket(6, 16, screenings.get(1), user4, order2, true),
                        new Ticket(5, 8, screenings.get(1), user4, order2, false),
                        new Ticket(8, 10, screenings.get(1), user4, order2, true),
                        new Ticket(7, 7, screenings.get(1), user4, order2, true),
                        new Ticket(7, 16, screenings.get(1), user4, order2, false),
                        new Ticket(3, 7, screenings.get(1), user4, order2, false),
                        new Ticket(12, 11, screenings.get(1), user4, order2, true),
                        new Ticket(7, 2, screenings.get(1), user4, order2, false),
                        new Ticket(8, 6, screenings.get(1), user4, order2, true),
                        new Ticket(9, 9, screenings.get(1), user4, order2, false),
                        new Ticket(7, 24, screenings.get(1), user4, order2, false),
                        new Ticket(7, 15, screenings.get(1), user4, order2, true),
                        new Ticket(4, 17, screenings.get(1), user4, order2, true),
                        new Ticket(10, 12, screenings.get(1), user4, order2, false),
                        new Ticket(6, 12, screenings.get(1), user4, order2, true),
                        new Ticket(9, 19, screenings.get(1), user4, order2, true),
                        new Ticket(8, 15, screenings.get(1), user4, order2, false),
                        new Ticket(10, 13, screenings.get(1), user4, order2, true),
                        new Ticket(8, 14, screenings.get(1), user4, order2, true),
                        new Ticket(8, 15, screenings.get(1), user4, order2, false),
                        new Ticket(7, 15, screenings.get(1), user4, order2, false),
                        new Ticket(9, 10, screenings.get(1), user4, order2, true),
                        new Ticket(6, 3, screenings.get(1), user4, order2, true),
                        new Ticket(8, 11, screenings.get(1), user4, order2, false),
                        new Ticket(10, 10, screenings.get(1), user4, order2, false),
                        new Ticket(7, 12, screenings.get(1), user4, order2, false),
                        new Ticket(7, 9, screenings.get(1), user4, order2, true),
                        new Ticket(8, 15, screenings.get(1), user4, order2, true),
                        new Ticket(7, 4, screenings.get(1), user4, order2, true),
                        new Ticket(6, 5, screenings.get(1), user4, order2, false),
                        new Ticket(10, 4, screenings.get(1), user4, order2, true),
                        new Ticket(6, 15, screenings.get(1), user4, order2, false),
                        new Ticket(7, 10, screenings.get(1), user4, order2, false),
                        new Ticket(8, 7, screenings.get(1), user4, order2, true),
                        new Ticket(7, 7, screenings.get(1), user4, order2, true),
                        new Ticket(10, 8, screenings.get(1), user4, order2, true),
                        new Ticket(9, 11, screenings.get(1), user4, order2, true),
                        new Ticket(6, 21, screenings.get(1), user4, order2, true),
                        new Ticket(8, 10, screenings.get(1), user4, order2, true),
                        new Ticket(12, 18, screenings.get(1), user4, order2, false),
                        new Ticket(8, 12, screenings.get(1), user4, order2, false)
                );
                List<Ticket> tickets3 = List.of(
                        new Ticket(8, 17, screenings.get(2), user5, order3, true),
                        new Ticket(9, 9, screenings.get(2), user5, order3, true),
                        new Ticket(4, 6, screenings.get(2), user5, order3, false),
                        new Ticket(9, 10, screenings.get(2), user5, order3, true),
                        new Ticket(8, 6, screenings.get(2), user5, order3, false),
                        new Ticket(5, 7, screenings.get(2), user5, order3, false),
                        new Ticket(2, 9, screenings.get(2), user5, order3, false),
                        new Ticket(6, 2, screenings.get(2), user5, order3, false),
                        new Ticket(4, 11, screenings.get(2), user5, order3, true),
                        new Ticket(5, 11, screenings.get(2), user5, order3, true),
                        new Ticket(5, 22, screenings.get(2), user5, order3, false),
                        new Ticket(9, 7, screenings.get(2), user5, order3, true),
                        new Ticket(8, 11, screenings.get(2), user5, order3, false),
                        new Ticket(6, 10, screenings.get(2), user5, order3, true),
                        new Ticket(6, 14, screenings.get(2), user5, order3, true),
                        new Ticket(5, 11, screenings.get(2), user5, order3, true),
                        new Ticket(6, 14, screenings.get(2), user5, order3, true),
                        new Ticket(4, 10, screenings.get(2), user5, order3, true),
                        new Ticket(8, 8, screenings.get(2), user5, order3, false),
                        new Ticket(5, 12, screenings.get(2), user5, order3, false),
                        new Ticket(6, 4, screenings.get(2), user5, order3, true),
                        new Ticket(4, 9, screenings.get(2), user5, order3, true),
                        new Ticket(4, 17, screenings.get(2), user5, order3, true),
                        new Ticket(7, 10, screenings.get(2), user5, order3, true),
                        new Ticket(5, 19, screenings.get(2), user5, order3, false),
                        new Ticket(5, 18, screenings.get(2), user5, order3, false),
                        new Ticket(6, 4, screenings.get(2), user5, order3, true),
                        new Ticket(7, 19, screenings.get(2), user5, order3, true),
                        new Ticket(7, 10, screenings.get(2), user5, order3, false),
                        new Ticket(7, 3, screenings.get(2), user5, order3, false),
                        new Ticket(3, 13, screenings.get(2), user5, order3, true),
                        new Ticket(6, 5, screenings.get(2), user5, order3, true),
                        new Ticket(2, 13, screenings.get(2), user5, order3, false),
                        new Ticket(4, 12, screenings.get(2), user5, order3, true),
                        new Ticket(6, 12, screenings.get(2), user5, order3, true),
                        new Ticket(5, 13, screenings.get(2), user5, order3, false),
                        new Ticket(6, 8, screenings.get(2), user5, order3, false),
                        new Ticket(6, 8, screenings.get(2), user5, order3, true),
                        new Ticket(6, 10, screenings.get(2), user5, order3, true),
                        new Ticket(6, 8, screenings.get(2), user5, order3, false),
                        new Ticket(5, 3, screenings.get(2), user5, order3, false),
                        new Ticket(2, 6, screenings.get(2), user5, order3, true),
                        new Ticket(3, 22, screenings.get(2), user5, order3, false),
                        new Ticket(11, 6, screenings.get(2), user5, order3, true),
                        new Ticket(4, 10, screenings.get(2), user5, order3, true),
                        new Ticket(6, 4, screenings.get(2), user5, order3, true),
                        new Ticket(4, 10, screenings.get(2), user5, order3, true),
                        new Ticket(5, 3, screenings.get(2), user5, order3, true),
                        new Ticket(4, 15, screenings.get(2), user5, order3, false),
                        new Ticket(5, 19, screenings.get(2), user5, order3, false),
                        new Ticket(4, 11, screenings.get(2), user5, order3, true),
                        new Ticket(6, 9, screenings.get(2), user5, order3, true),
                        new Ticket(4, 3, screenings.get(2), user5, order3, true),
                        new Ticket(3, 14, screenings.get(2), user5, order3, true),
                        new Ticket(5, 13, screenings.get(2), user5, order3, true),
                        new Ticket(7, 5, screenings.get(2), user5, order3, true),
                        new Ticket(7, 12, screenings.get(2), user5, order3, true),
                        new Ticket(6, 9, screenings.get(2), user5, order3, false),
                        new Ticket(8, 15, screenings.get(2), user5, order3, false),
                        new Ticket(6, 12, screenings.get(2), user5, order3, true),
                        new Ticket(5, 4, screenings.get(2), user5, order3, true),
                        new Ticket(4, 6, screenings.get(2), user5, order3, true),
                        new Ticket(5, 15, screenings.get(2), user5, order3, true),
                        new Ticket(5, 17, screenings.get(2), user5, order3, true),
                        new Ticket(4, 10, screenings.get(2), user5, order3, false),
                        new Ticket(4, 9, screenings.get(2), user5, order3, true),
                        new Ticket(5, 12, screenings.get(2), user5, order3, false),
                        new Ticket(8, 10, screenings.get(2), user5, order3, false),
                        new Ticket(7, 9, screenings.get(2), user5, order3, true),
                        new Ticket(5, 5, screenings.get(2), user5, order3, true),
                        new Ticket(8, 12, screenings.get(2), user5, order3, true),
                        new Ticket(6, 10, screenings.get(2), user5, order3, true),
                        new Ticket(6, 8, screenings.get(2), user5, order3, true),
                        new Ticket(8, 12, screenings.get(2), user5, order3, true),
                        new Ticket(5, 6, screenings.get(2), user5, order3, false),
                        new Ticket(3, 6, screenings.get(2), user5, order3, false),
                        new Ticket(6, 22, screenings.get(2), user5, order3, true),
                        new Ticket(8, 5, screenings.get(2), user5, order3, false),
                        new Ticket(11, 20, screenings.get(2), user5, order3, true),
                        new Ticket(9, 17, screenings.get(2), user5, order3, false),
                        new Ticket(10, 12, screenings.get(2), user5, order3, false),
                        new Ticket(6, 12, screenings.get(2), user5, order3, true),
                        new Ticket(6, 8, screenings.get(2), user5, order3, false),
                        new Ticket(9, 13, screenings.get(2), user5, order3, false),
                        new Ticket(4, 8, screenings.get(2), user5, order3, true),
                        new Ticket(5, 10, screenings.get(2), user5, order3, false),
                        new Ticket(6, 7, screenings.get(2), user5, order3, true),
                        new Ticket(7, 5, screenings.get(2), user5, order3, true),
                        new Ticket(4, 9, screenings.get(2), user5, order3, true),
                        new Ticket(4, 16, screenings.get(2), user5, order3, false),
                        new Ticket(7, 16, screenings.get(2), user5, order3, true),
                        new Ticket(9, 15, screenings.get(2), user5, order3, true),
                        new Ticket(6, 15, screenings.get(2), user5, order3, false),
                        new Ticket(4, 15, screenings.get(2), user5, order3, true),
                        new Ticket(8, 20, screenings.get(2), user5, order3, true),
                        new Ticket(4, 13, screenings.get(2), user5, order3, true)
                );
                List<Ticket> tickets4 = List.of(
                        new Ticket(3, 4, screenings.get(3), user6, order4, false),
                        new Ticket(4, 18, screenings.get(3), user6, order4, false),
                        new Ticket(2, 11, screenings.get(3), user6, order4, false),
                        new Ticket(1, 5, screenings.get(3), user6, order4, true),
                        new Ticket(1, 18, screenings.get(3), user6, order4, false),
                        new Ticket(6, 16, screenings.get(3), user6, order4, false),
                        new Ticket(5, 12, screenings.get(3), user6, order4, false),
                        new Ticket(5, 12, screenings.get(3), user6, order4, true),
                        new Ticket(5, 9, screenings.get(3), user6, order4, true),
                        new Ticket(5, 15, screenings.get(3), user6, order4, false),
                        new Ticket(5, 2, screenings.get(3), user6, order4, true),
                        new Ticket(1, 14, screenings.get(3), user6, order4, false),
                        new Ticket(8, 15, screenings.get(3), user6, order4, false),
                        new Ticket(5, 12, screenings.get(3), user6, order4, false),
                        new Ticket(2, 12, screenings.get(3), user6, order4, false),
                        new Ticket(2, 6, screenings.get(3), user6, order4, false),
                        new Ticket(3, 11, screenings.get(3), user6, order4, true),
                        new Ticket(5, 7, screenings.get(3), user6, order4, true),
                        new Ticket(5, 7, screenings.get(3), user6, order4, true),
                        new Ticket(1, 3, screenings.get(3), user6, order4, false),
                        new Ticket(5, 11, screenings.get(3), user6, order4, true),
                        new Ticket(2, 18, screenings.get(3), user6, order4, false),
                        new Ticket(4, 13, screenings.get(3), user6, order4, false),
                        new Ticket(4, 18, screenings.get(3), user6, order4, true),
                        new Ticket(5, 7, screenings.get(3), user6, order4, true),
                        new Ticket(8, 7, screenings.get(3), user6, order4, false),
                        new Ticket(6, 14, screenings.get(3), user6, order4, false),
                        new Ticket(2, 9, screenings.get(3), user6, order4, false),
                        new Ticket(5, 1, screenings.get(3), user6, order4, false),
                        new Ticket(5, 10, screenings.get(3), user6, order4, true),
                        new Ticket(4, 11, screenings.get(3), user6, order4, true),
                        new Ticket(5, 10, screenings.get(3), user6, order4, true),
                        new Ticket(6, 14, screenings.get(3), user6, order4, true),
                        new Ticket(2, 3, screenings.get(3), user6, order4, false),
                        new Ticket(4, 5, screenings.get(3), user6, order4, true),
                        new Ticket(2, 7, screenings.get(3), user6, order4, false),
                        new Ticket(6, 6, screenings.get(3), user6, order4, false),
                        new Ticket(3, 12, screenings.get(3), user6, order4, true),
                        new Ticket(6, 15, screenings.get(3), user6, order4, true),
                        new Ticket(5, 12, screenings.get(3), user6, order4, false),
                        new Ticket(3, 14, screenings.get(3), user6, order4, true),
                        new Ticket(5, 8, screenings.get(3), user6, order4, false),
                        new Ticket(1, 7, screenings.get(3), user6, order4, true),
                        new Ticket(5, 3, screenings.get(3), user6, order4, false),
                        new Ticket(4, 14, screenings.get(3), user6, order4, true),
                        new Ticket(5, 17, screenings.get(3), user6, order4, true),
                        new Ticket(1, 5, screenings.get(3), user6, order4, false),
                        new Ticket(3, 6, screenings.get(3), user6, order4, true)
                );
                List<Ticket> tickets5 = List.of(
                        new Ticket(8, 14, screenings.get(4), user7, order5, false),
                        new Ticket(6, 15, screenings.get(4), user7, order5, false),
                        new Ticket(6, 11, screenings.get(4), user7, order5, false),
                        new Ticket(8, 24, screenings.get(4), user7, order5, false),
                        new Ticket(8, 21, screenings.get(4), user7, order5, false),
                        new Ticket(4, 15, screenings.get(4), user7, order5, false),
                        new Ticket(8, 17, screenings.get(4), user7, order5, true),
                        new Ticket(6, 13, screenings.get(4), user7, order5, true),
                        new Ticket(7, 15, screenings.get(4), user7, order5, true),
                        new Ticket(4, 15, screenings.get(4), user7, order5, true),
                        new Ticket(6, 23, screenings.get(4), user7, order5, false),
                        new Ticket(9, 11, screenings.get(4), user7, order5, false),
                        new Ticket(6, 17, screenings.get(4), user7, order5, true),
                        new Ticket(6, 12, screenings.get(4), user7, order5, true),
                        new Ticket(5, 16, screenings.get(4), user7, order5, false),
                        new Ticket(6, 16, screenings.get(4), user7, order5, true),
                        new Ticket(7, 14, screenings.get(4), user7, order5, true),
                        new Ticket(7, 22, screenings.get(4), user7, order5, true),
                        new Ticket(5, 11, screenings.get(4), user7, order5, true),
                        new Ticket(5, 19, screenings.get(4), user7, order5, true),
                        new Ticket(6, 21, screenings.get(4), user7, order5, true),
                        new Ticket(6, 12, screenings.get(4), user7, order5, false),
                        new Ticket(8, 16, screenings.get(4), user7, order5, false),
                        new Ticket(10, 13, screenings.get(4), user7, order5, true),
                        new Ticket(7, 17, screenings.get(4), user7, order5, false),
                        new Ticket(9, 12, screenings.get(4), user7, order5, false),
                        new Ticket(8, 16, screenings.get(4), user7, order5, true),
                        new Ticket(6, 11, screenings.get(4), user7, order5, false),
                        new Ticket(6, 18, screenings.get(4), user7, order5, true),
                        new Ticket(3, 11, screenings.get(4), user7, order5, false),
                        new Ticket(8, 19, screenings.get(4), user7, order5, true),
                        new Ticket(9, 8, screenings.get(4), user7, order5, false),
                        new Ticket(7, 7, screenings.get(4), user7, order5, false),
                        new Ticket(7, 7, screenings.get(4), user7, order5, false),
                        new Ticket(9, 23, screenings.get(4), user7, order5, true),
                        new Ticket(7, 10, screenings.get(4), user7, order5, false),
                        new Ticket(8, 18, screenings.get(4), user7, order5, true),
                        new Ticket(5, 11, screenings.get(4), user7, order5, false),
                        new Ticket(9, 19, screenings.get(4), user7, order5, true),
                        new Ticket(7, 10, screenings.get(4), user7, order5, false),
                        new Ticket(6, 18, screenings.get(4), user7, order5, true),
                        new Ticket(6, 13, screenings.get(4), user7, order5, false),
                        new Ticket(8, 15, screenings.get(4), user7, order5, true),
                        new Ticket(9, 14, screenings.get(4), user7, order5, true),
                        new Ticket(6, 19, screenings.get(4), user7, order5, false),
                        new Ticket(5, 17, screenings.get(4), user7, order5, false),
                        new Ticket(9, 12, screenings.get(4), user7, order5, false),
                        new Ticket(8, 8, screenings.get(4), user7, order5, false),
                        new Ticket(4, 15, screenings.get(4), user7, order5, true),
                        new Ticket(4, 13, screenings.get(4), user7, order5, true),
                        new Ticket(9, 11, screenings.get(4), user7, order5, true),
                        new Ticket(8, 15, screenings.get(4), user7, order5, true),
                        new Ticket(8, 20, screenings.get(4), user7, order5, false),
                        new Ticket(5, 27, screenings.get(4), user7, order5, true),
                        new Ticket(10, 14, screenings.get(4), user7, order5, false),
                        new Ticket(7, 21, screenings.get(4), user7, order5, false),
                        new Ticket(4, 14, screenings.get(4), user7, order5, false),
                        new Ticket(8, 22, screenings.get(4), user7, order5, true),
                        new Ticket(6, 8, screenings.get(4), user7, order5, false),
                        new Ticket(5, 10, screenings.get(4), user7, order5, false),
                        new Ticket(8, 16, screenings.get(4), user7, order5, true),
                        new Ticket(6, 14, screenings.get(4), user7, order5, false),
                        new Ticket(7, 17, screenings.get(4), user7, order5, false),
                        new Ticket(9, 22, screenings.get(4), user7, order5, false),
                        new Ticket(5, 14, screenings.get(4), user7, order5, true),
                        new Ticket(7, 14, screenings.get(4), user7, order5, false),
                        new Ticket(4, 21, screenings.get(4), user7, order5, false),
                        new Ticket(2, 16, screenings.get(4), user7, order5, true),
                        new Ticket(5, 18, screenings.get(4), user7, order5, false),
                        new Ticket(11, 19, screenings.get(4), user7, order5, true),
                        new Ticket(8, 20, screenings.get(4), user7, order5, false),
                        new Ticket(10, 13, screenings.get(4), user7, order5, true),
                        new Ticket(11, 19, screenings.get(4), user7, order5, false),
                        new Ticket(8, 9, screenings.get(4), user7, order5, true),
                        new Ticket(6, 22, screenings.get(4), user7, order5, true),
                        new Ticket(5, 11, screenings.get(4), user7, order5, false),
                        new Ticket(8, 14, screenings.get(4), user7, order5, false),
                        new Ticket(8, 18, screenings.get(4), user7, order5, false)
                );
                List<Ticket> tickets6 = List.of(
                        new Ticket(8, 13, screenings.get(5), user8, order6, false),
                        new Ticket(6, 16, screenings.get(5), user8, order6, false),
                        new Ticket(8, 13, screenings.get(5), user8, order6, false),
                        new Ticket(7, 13, screenings.get(5), user8, order6, true),
                        new Ticket(5, 10, screenings.get(5), user8, order6, true),
                        new Ticket(10, 18, screenings.get(5), user8, order6, false),
                        new Ticket(5, 9, screenings.get(5), user8, order6, false),
                        new Ticket(5, 10, screenings.get(5), user8, order6, false),
                        new Ticket(9, 19, screenings.get(5), user8, order6, false),
                        new Ticket(5, 17, screenings.get(5), user8, order6, true),
                        new Ticket(12, 16, screenings.get(5), user8, order6, true),
                        new Ticket(7, 10, screenings.get(5), user8, order6, false),
                        new Ticket(9, 10, screenings.get(5), user8, order6, true),
                        new Ticket(10, 25, screenings.get(5), user8, order6, true),
                        new Ticket(13, 19, screenings.get(5), user8, order6, true),
                        new Ticket(5, 13, screenings.get(5), user8, order6, false),
                        new Ticket(9, 17, screenings.get(5), user8, order6, true),
                        new Ticket(9, 8, screenings.get(5), user8, order6, false),
                        new Ticket(11, 8, screenings.get(5), user8, order6, false),
                        new Ticket(6, 17, screenings.get(5), user8, order6, false),
                        new Ticket(9, 15, screenings.get(5), user8, order6, true),
                        new Ticket(5, 10, screenings.get(5), user8, order6, false),
                        new Ticket(9, 18, screenings.get(5), user8, order6, true),
                        new Ticket(7, 20, screenings.get(5), user8, order6, false),
                        new Ticket(8, 14, screenings.get(5), user8, order6, false),
                        new Ticket(10, 17, screenings.get(5), user8, order6, false),
                        new Ticket(6, 23, screenings.get(5), user8, order6, true),
                        new Ticket(8, 15, screenings.get(5), user8, order6, true),
                        new Ticket(9, 15, screenings.get(5), user8, order6, true),
                        new Ticket(6, 10, screenings.get(5), user8, order6, false),
                        new Ticket(4, 4, screenings.get(5), user8, order6, true),
                        new Ticket(7, 7, screenings.get(5), user8, order6, true),
                        new Ticket(9, 7, screenings.get(5), user8, order6, false),
                        new Ticket(6, 10, screenings.get(5), user8, order6, false),
                        new Ticket(3, 14, screenings.get(5), user8, order6, false),
                        new Ticket(6, 20, screenings.get(5), user8, order6, false),
                        new Ticket(7, 21, screenings.get(5), user8, order6, false),
                        new Ticket(7, 1, screenings.get(5), user8, order6, false),
                        new Ticket(8, 9, screenings.get(5), user8, order6, false),
                        new Ticket(10, 16, screenings.get(5), user8, order6, false),
                        new Ticket(7, 16, screenings.get(5), user8, order6, false),
                        new Ticket(8, 10, screenings.get(5), user8, order6, true),
                        new Ticket(8, 15, screenings.get(5), user8, order6, true),
                        new Ticket(5, 15, screenings.get(5), user8, order6, false),
                        new Ticket(9, 15, screenings.get(5), user8, order6, true),
                        new Ticket(10, 13, screenings.get(5), user8, order6, false),
                        new Ticket(11, 10, screenings.get(5), user8, order6, true),
                        new Ticket(7, 10, screenings.get(5), user8, order6, true),
                        new Ticket(8, 14, screenings.get(5), user8, order6, false),
                        new Ticket(8, 17, screenings.get(5), user8, order6, false)
                );
                List<Ticket> tickets7 = List.of(
                        new Ticket(4, 3, screenings.get(6), user9, order7, false),
                        new Ticket(7, 2, screenings.get(6), user9, order7, false),
                        new Ticket(6, 11, screenings.get(6), user9, order7, false),
                        new Ticket(4, 14, screenings.get(6), user9, order7, false),
                        new Ticket(3, 4, screenings.get(6), user9, order7, true),
                        new Ticket(10, 6, screenings.get(6), user9, order7, false),
                        new Ticket(5, 5, screenings.get(6), user9, order7, true),
                        new Ticket(3, 12, screenings.get(6), user9, order7, false),
                        new Ticket(6, 12, screenings.get(6), user9, order7, true),
                        new Ticket(7, 13, screenings.get(6), user9, order7, false),
                        new Ticket(5, 12, screenings.get(6), user9, order7, true),
                        new Ticket(6, 8, screenings.get(6), user9, order7, true),
                        new Ticket(3, 20, screenings.get(6), user9, order7, true),
                        new Ticket(4, 7, screenings.get(6), user9, order7, true),
                        new Ticket(3, 11, screenings.get(6), user9, order7, true),
                        new Ticket(8, 11, screenings.get(6), user9, order7, false),
                        new Ticket(2, 20, screenings.get(6), user9, order7, true),
                        new Ticket(4, 3, screenings.get(6), user9, order7, false),
                        new Ticket(10, 15, screenings.get(6), user9, order7, false),
                        new Ticket(5, 18, screenings.get(6), user9, order7, false),
                        new Ticket(6, 9, screenings.get(6), user9, order7, false),
                        new Ticket(5, 14, screenings.get(6), user9, order7, false),
                        new Ticket(4, 11, screenings.get(6), user9, order7, true),
                        new Ticket(5, 17, screenings.get(6), user9, order7, true),
                        new Ticket(2, 1, screenings.get(6), user9, order7, true),
                        new Ticket(3, 14, screenings.get(6), user9, order7, false),
                        new Ticket(4, 15, screenings.get(6), user9, order7, false),
                        new Ticket(5, 12, screenings.get(6), user9, order7, false),
                        new Ticket(6, 3, screenings.get(6), user9, order7, false),
                        new Ticket(2, 15, screenings.get(6), user9, order7, true),
                        new Ticket(3, 4, screenings.get(6), user9, order7, false),
                        new Ticket(4, 10, screenings.get(6), user9, order7, false),
                        new Ticket(8, 15, screenings.get(6), user9, order7, false),
                        new Ticket(5, 8, screenings.get(6), user9, order7, false),
                        new Ticket(7, 10, screenings.get(6), user9, order7, true),
                        new Ticket(3, 15, screenings.get(6), user9, order7, true),
                        new Ticket(5, 18, screenings.get(6), user9, order7, false),
                        new Ticket(2, 9, screenings.get(6), user9, order7, true),
                        new Ticket(3, 9, screenings.get(6), user9, order7, true),
                        new Ticket(6, 16, screenings.get(6), user9, order7, true),
                        new Ticket(3, 11, screenings.get(6), user9, order7, true),
                        new Ticket(5, 19, screenings.get(6), user9, order7, false),
                        new Ticket(7, 11, screenings.get(6), user9, order7, false),
                        new Ticket(4, 13, screenings.get(6), user9, order7, true),
                        new Ticket(5, 3, screenings.get(6), user9, order7, true),
                        new Ticket(6, 11, screenings.get(6), user9, order7, false),
                        new Ticket(8, 11, screenings.get(6), user9, order7, false),
                        new Ticket(8, 4, screenings.get(6), user9, order7, true),
                        new Ticket(6, 15, screenings.get(6), user9, order7, false),
                        new Ticket(3, 13, screenings.get(6), user9, order7, true),
                        new Ticket(4, 5, screenings.get(6), user9, order7, false),
                        new Ticket(3, 3, screenings.get(6), user9, order7, false),
                        new Ticket(7, 5, screenings.get(6), user9, order7, true),
                        new Ticket(7, 10, screenings.get(6), user9, order7, true),
                        new Ticket(4, 10, screenings.get(6), user9, order7, true),
                        new Ticket(7, 16, screenings.get(6), user9, order7, true),
                        new Ticket(6, 8, screenings.get(6), user9, order7, false),
                        new Ticket(10, 6, screenings.get(6), user9, order7, false),
                        new Ticket(6, 8, screenings.get(6), user9, order7, true),
                        new Ticket(1, 1, screenings.get(6), user9, order7, false),
                        new Ticket(5, 10, screenings.get(6), user9, order7, false),
                        new Ticket(6, 20, screenings.get(6), user9, order7, false),
                        new Ticket(6, 6, screenings.get(6), user9, order7, true),
                        new Ticket(5, 1, screenings.get(6), user9, order7, false),
                        new Ticket(4, 11, screenings.get(6), user9, order7, true),
                        new Ticket(5, 11, screenings.get(6), user9, order7, true),
                        new Ticket(6, 5, screenings.get(6), user9, order7, true),
                        new Ticket(4, 12, screenings.get(6), user9, order7, true),
                        new Ticket(6, 18, screenings.get(6), user9, order7, false),
                        new Ticket(5, 6, screenings.get(6), user9, order7, false),
                        new Ticket(4, 8, screenings.get(6), user9, order7, false),
                        new Ticket(7, 7, screenings.get(6), user9, order7, false),
                        new Ticket(6, 7, screenings.get(6), user9, order7, false),
                        new Ticket(7, 12, screenings.get(6), user9, order7, false),
                        new Ticket(6, 14, screenings.get(6), user9, order7, true),
                        new Ticket(5, 10, screenings.get(6), user9, order7, true),
                        new Ticket(3, 9, screenings.get(6), user9, order7, false),
                        new Ticket(8, 13, screenings.get(6), user9, order7, true),
                        new Ticket(5, 20, screenings.get(6), user9, order7, false),
                        new Ticket(3, 13, screenings.get(6), user9, order7, true)
                );
                List<Ticket> tickets8 = List.of(
                        new Ticket(7, 16, screenings.get(7), user10, order8, false),
                        new Ticket(4, 12, screenings.get(7), user10, order8, true),
                        new Ticket(3, 11, screenings.get(7), user10, order8, false),
                        new Ticket(8, 2, screenings.get(7), user10, order8, true),
                        new Ticket(5, 13, screenings.get(7), user10, order8, true),
                        new Ticket(6, 10, screenings.get(7), user10, order8, true),
                        new Ticket(4, 14, screenings.get(7), user10, order8, true),
                        new Ticket(7, 10, screenings.get(7), user10, order8, true),
                        new Ticket(11, 12, screenings.get(7), user10, order8, true),
                        new Ticket(4, 14, screenings.get(7), user10, order8, true),
                        new Ticket(8, 19, screenings.get(7), user10, order8, true),
                        new Ticket(7, 4, screenings.get(7), user10, order8, false),
                        new Ticket(6, 10, screenings.get(7), user10, order8, true),
                        new Ticket(8, 7, screenings.get(7), user10, order8, false),
                        new Ticket(7, 16, screenings.get(7), user10, order8, false),
                        new Ticket(3, 8, screenings.get(7), user10, order8, false),
                        new Ticket(7, 16, screenings.get(7), user10, order8, true),
                        new Ticket(7, 10, screenings.get(7), user10, order8, true),
                        new Ticket(7, 9, screenings.get(7), user10, order8, true),
                        new Ticket(7, 16, screenings.get(7), user10, order8, true),
                        new Ticket(8, 11, screenings.get(7), user10, order8, true),
                        new Ticket(7, 9, screenings.get(7), user10, order8, false),
                        new Ticket(4, 13, screenings.get(7), user10, order8, false),
                        new Ticket(6, 19, screenings.get(7), user10, order8, false),
                        new Ticket(4, 17, screenings.get(7), user10, order8, false),
                        new Ticket(7, 14, screenings.get(7), user10, order8, true),
                        new Ticket(6, 13, screenings.get(7), user10, order8, false),
                        new Ticket(6, 14, screenings.get(7), user10, order8, false),
                        new Ticket(7, 15, screenings.get(7), user10, order8, true),
                        new Ticket(8, 12, screenings.get(7), user10, order8, false),
                        new Ticket(7, 7, screenings.get(7), user10, order8, false),
                        new Ticket(2, 6, screenings.get(7), user10, order8, true),
                        new Ticket(6, 18, screenings.get(7), user10, order8, false),
                        new Ticket(7, 2, screenings.get(7), user10, order8, true),
                        new Ticket(7, 7, screenings.get(7), user10, order8, false),
                        new Ticket(10, 6, screenings.get(7), user10, order8, false),
                        new Ticket(8, 22, screenings.get(7), user10, order8, false),
                        new Ticket(5, 13, screenings.get(7), user10, order8, false),
                        new Ticket(6, 6, screenings.get(7), user10, order8, true),
                        new Ticket(4, 13, screenings.get(7), user10, order8, true),
                        new Ticket(4, 13, screenings.get(7), user10, order8, true),
                        new Ticket(7, 16, screenings.get(7), user10, order8, false),
                        new Ticket(4, 12, screenings.get(7), user10, order8, false),
                        new Ticket(6, 1, screenings.get(7), user10, order8, false),
                        new Ticket(5, 10, screenings.get(7), user10, order8, false),
                        new Ticket(6, 9, screenings.get(7), user10, order8, false),
                        new Ticket(8, 10, screenings.get(7), user10, order8, false),
                        new Ticket(10, 14, screenings.get(7), user10, order8, true),
                        new Ticket(9, 16, screenings.get(7), user10, order8, true),
                        new Ticket(8, 7, screenings.get(7), user10, order8, true),
                        new Ticket(4, 10, screenings.get(7), user10, order8, false),
                        new Ticket(6, 7, screenings.get(7), user10, order8, false),
                        new Ticket(10, 16, screenings.get(7), user10, order8, true),
                        new Ticket(6, 2, screenings.get(7), user10, order8, true),
                        new Ticket(2, 10, screenings.get(7), user10, order8, true),
                        new Ticket(5, 12, screenings.get(7), user10, order8, true),
                        new Ticket(4, 12, screenings.get(7), user10, order8, true),
                        new Ticket(8, 9, screenings.get(7), user10, order8, false),
                        new Ticket(8, 10, screenings.get(7), user10, order8, false),
                        new Ticket(7, 14, screenings.get(7), user10, order8, false),
                        new Ticket(7, 17, screenings.get(7), user10, order8, false),
                        new Ticket(4, 12, screenings.get(7), user10, order8, true)
                );
                List<Ticket> tickets9 = List.of(
                        new Ticket(6, 9, screenings.get(8), user11, order9, false),
                        new Ticket(9, 9, screenings.get(8), user11, order9, false),
                        new Ticket(11, 13, screenings.get(8), user11, order9, true),
                        new Ticket(2, 10, screenings.get(8), user11, order9, true),
                        new Ticket(5, 15, screenings.get(8), user11, order9, false),
                        new Ticket(6, 18, screenings.get(8), user11, order9, true),
                        new Ticket(5, 18, screenings.get(8), user11, order9, true),
                        new Ticket(4, 5, screenings.get(8), user11, order9, false),
                        new Ticket(10, 12, screenings.get(8), user11, order9, false),
                        new Ticket(6, 15, screenings.get(8), user11, order9, true),
                        new Ticket(6, 5, screenings.get(8), user11, order9, false),
                        new Ticket(6, 18, screenings.get(8), user11, order9, false),
                        new Ticket(3, 15, screenings.get(8), user11, order9, true),
                        new Ticket(6, 17, screenings.get(8), user11, order9, true),
                        new Ticket(5, 8, screenings.get(8), user11, order9, false),
                        new Ticket(6, 6, screenings.get(8), user11, order9, true),
                        new Ticket(3, 19, screenings.get(8), user11, order9, true),
                        new Ticket(9, 8, screenings.get(8), user11, order9, true),
                        new Ticket(9, 7, screenings.get(8), user11, order9, false),
                        new Ticket(3, 17, screenings.get(8), user11, order9, false),
                        new Ticket(4, 13, screenings.get(8), user11, order9, false),
                        new Ticket(7, 14, screenings.get(8), user11, order9, true),
                        new Ticket(7, 11, screenings.get(8), user11, order9, true),
                        new Ticket(7, 15, screenings.get(8), user11, order9, false),
                        new Ticket(4, 14, screenings.get(8), user11, order9, false),
                        new Ticket(6, 15, screenings.get(8), user11, order9, true),
                        new Ticket(3, 20, screenings.get(8), user11, order9, false),
                        new Ticket(5, 17, screenings.get(8), user11, order9, false),
                        new Ticket(8, 9, screenings.get(8), user11, order9, false),
                        new Ticket(5, 10, screenings.get(8), user11, order9, true),
                        new Ticket(11, 8, screenings.get(8), user11, order9, false),
                        new Ticket(4, 13, screenings.get(8), user11, order9, false),
                        new Ticket(6, 11, screenings.get(8), user11, order9, false),
                        new Ticket(5, 8, screenings.get(8), user11, order9, false),
                        new Ticket(6, 10, screenings.get(8), user11, order9, false),
                        new Ticket(8, 8, screenings.get(8), user11, order9, true),
                        new Ticket(5, 5, screenings.get(8), user11, order9, false),
                        new Ticket(4, 16, screenings.get(8), user11, order9, false),
                        new Ticket(5, 11, screenings.get(8), user11, order9, false),
                        new Ticket(7, 7, screenings.get(8), user11, order9, true),
                        new Ticket(6, 18, screenings.get(8), user11, order9, false),
                        new Ticket(6, 14, screenings.get(8), user11, order9, false),
                        new Ticket(6, 5, screenings.get(8), user11, order9, false),
                        new Ticket(7, 16, screenings.get(8), user11, order9, false),
                        new Ticket(5, 18, screenings.get(8), user11, order9, true),
                        new Ticket(6, 14, screenings.get(8), user11, order9, true),
                        new Ticket(6, 15, screenings.get(8), user11, order9, true),
                        new Ticket(6, 16, screenings.get(8), user11, order9, true),
                        new Ticket(8, 15, screenings.get(8), user11, order9, false),
                        new Ticket(4, 14, screenings.get(8), user11, order9, true),
                        new Ticket(6, 11, screenings.get(8), user11, order9, false),
                        new Ticket(9, 13, screenings.get(8), user11, order9, false),
                        new Ticket(6, 10, screenings.get(8), user11, order9, false),
                        new Ticket(6, 9, screenings.get(8), user11, order9, false),
                        new Ticket(5, 11, screenings.get(8), user11, order9, true),
                        new Ticket(7, 1, screenings.get(8), user11, order9, false),
                        new Ticket(9, 8, screenings.get(8), user11, order9, true),
                        new Ticket(4, 17, screenings.get(8), user11, order9, false),
                        new Ticket(8, 6, screenings.get(8), user11, order9, false),
                        new Ticket(4, 19, screenings.get(8), user11, order9, false),
                        new Ticket(9, 6, screenings.get(8), user11, order9, false),
                        new Ticket(4, 20, screenings.get(8), user11, order9, false),
                        new Ticket(7, 6, screenings.get(8), user11, order9, false),
                        new Ticket(8, 7, screenings.get(8), user11, order9, true),
                        new Ticket(5, 11, screenings.get(8), user11, order9, true),
                        new Ticket(8, 13, screenings.get(8), user11, order9, true),
                        new Ticket(7, 16, screenings.get(8), user11, order9, false),
                        new Ticket(2, 4, screenings.get(8), user11, order9, true),
                        new Ticket(6, 11, screenings.get(8), user11, order9, true),
                        new Ticket(4, 8, screenings.get(8), user11, order9, true),
                        new Ticket(5, 12, screenings.get(8), user11, order9, true)
                );
                List<Ticket> tickets10 = List.of(
                        new Ticket(9, 9, screenings.get(9), user12, order10, false),
                        new Ticket(7, 17, screenings.get(9), user12, order10, false),
                        new Ticket(8, 13, screenings.get(9), user12, order10, false),
                        new Ticket(4, 9, screenings.get(9), user12, order10, true),
                        new Ticket(8, 15, screenings.get(9), user12, order10, true),
                        new Ticket(8, 15, screenings.get(9), user12, order10, true),
                        new Ticket(9, 13, screenings.get(9), user12, order10, false),
                        new Ticket(9, 14, screenings.get(9), user12, order10, false),
                        new Ticket(10, 12, screenings.get(9), user12, order10, true),
                        new Ticket(8, 8, screenings.get(9), user12, order10, true),
                        new Ticket(5, 8, screenings.get(9), user12, order10, false),
                        new Ticket(8, 15, screenings.get(9), user12, order10, false),
                        new Ticket(11, 10, screenings.get(9), user12, order10, true),
                        new Ticket(11, 11, screenings.get(9), user12, order10, true),
                        new Ticket(7, 9, screenings.get(9), user12, order10, false),
                        new Ticket(9, 6, screenings.get(9), user12, order10, false),
                        new Ticket(8, 16, screenings.get(9), user12, order10, true),
                        new Ticket(11, 9, screenings.get(9), user12, order10, false),
                        new Ticket(9, 17, screenings.get(9), user12, order10, true),
                        new Ticket(8, 2, screenings.get(9), user12, order10, true),
                        new Ticket(7, 1, screenings.get(9), user12, order10, true),
                        new Ticket(8, 13, screenings.get(9), user12, order10, false),
                        new Ticket(9, 18, screenings.get(9), user12, order10, true),
                        new Ticket(9, 11, screenings.get(9), user12, order10, true),
                        new Ticket(8, 17, screenings.get(9), user12, order10, true),
                        new Ticket(5, 21, screenings.get(9), user12, order10, true),
                        new Ticket(5, 8, screenings.get(9), user12, order10, false),
                        new Ticket(5, 17, screenings.get(9), user12, order10, true),
                        new Ticket(2, 10, screenings.get(9), user12, order10, true),
                        new Ticket(5, 7, screenings.get(9), user12, order10, true),
                        new Ticket(10, 14, screenings.get(9), user12, order10, true),
                        new Ticket(9, 17, screenings.get(9), user12, order10, false),
                        new Ticket(10, 10, screenings.get(9), user12, order10, false),
                        new Ticket(10, 14, screenings.get(9), user12, order10, true),
                        new Ticket(9, 5, screenings.get(9), user12, order10, true),
                        new Ticket(8, 10, screenings.get(9), user12, order10, false),
                        new Ticket(7, 22, screenings.get(9), user12, order10, true),
                        new Ticket(11, 10, screenings.get(9), user12, order10, true),
                        new Ticket(6, 8, screenings.get(9), user12, order10, false),
                        new Ticket(8, 15, screenings.get(9), user12, order10, true),
                        new Ticket(6, 11, screenings.get(9), user12, order10, false),
                        new Ticket(8, 5, screenings.get(9), user12, order10, true),
                        new Ticket(7, 12, screenings.get(9), user12, order10, false),
                        new Ticket(7, 25, screenings.get(9), user12, order10, false),
                        new Ticket(6, 21, screenings.get(9), user12, order10, false),
                        new Ticket(10, 24, screenings.get(9), user12, order10, true),
                        new Ticket(5, 15, screenings.get(9), user12, order10, false),
                        new Ticket(10, 15, screenings.get(9), user12, order10, true),
                        new Ticket(9, 9, screenings.get(9), user12, order10, true),
                        new Ticket(8, 12, screenings.get(9), user12, order10, false),
                        new Ticket(7, 16, screenings.get(9), user12, order10, false),
                        new Ticket(8, 18, screenings.get(9), user12, order10, true),
                        new Ticket(6, 10, screenings.get(9), user12, order10, false),
                        new Ticket(4, 16, screenings.get(9), user12, order10, false),
                        new Ticket(4, 17, screenings.get(9), user12, order10, false),
                        new Ticket(8, 17, screenings.get(9), user12, order10, true),
                        new Ticket(8, 17, screenings.get(9), user12, order10, true),
                        new Ticket(7, 15, screenings.get(9), user12, order10, false),
                        new Ticket(8, 6, screenings.get(9), user12, order10, true),
                        new Ticket(6, 12, screenings.get(9), user12, order10, true),
                        new Ticket(5, 9, screenings.get(9), user12, order10, false),
                        new Ticket(6, 13, screenings.get(9), user12, order10, false),
                        new Ticket(8, 9, screenings.get(9), user12, order10, false),
                        new Ticket(8, 20, screenings.get(9), user12, order10, false),
                        new Ticket(8, 2, screenings.get(9), user12, order10, true),
                        new Ticket(5, 5, screenings.get(9), user12, order10, true),
                        new Ticket(8, 19, screenings.get(9), user12, order10, true),
                        new Ticket(6, 16, screenings.get(9), user12, order10, true),
                        new Ticket(6, 21, screenings.get(9), user12, order10, true),
                        new Ticket(7, 19, screenings.get(9), user12, order10, true),
                        new Ticket(10, 10, screenings.get(9), user12, order10, false),
                        new Ticket(9, 19, screenings.get(9), user12, order10, false),
                        new Ticket(6, 10, screenings.get(9), user12, order10, true),
                        new Ticket(7, 9, screenings.get(9), user12, order10, false),
                        new Ticket(5, 15, screenings.get(9), user12, order10, true),
                        new Ticket(4, 8, screenings.get(9), user12, order10, true),
                        new Ticket(12, 19, screenings.get(9), user12, order10, false),
                        new Ticket(8, 11, screenings.get(9), user12, order10, false),
                        new Ticket(9, 13, screenings.get(9), user12, order10, false),
                        new Ticket(8, 9, screenings.get(9), user12, order10, true),
                        new Ticket(8, 16, screenings.get(9), user12, order10, false),
                        new Ticket(7, 12, screenings.get(9), user12, order10, false),
                        new Ticket(7, 9, screenings.get(9), user12, order10, true),
                        new Ticket(9, 14, screenings.get(9), user12, order10, false),
                        new Ticket(6, 13, screenings.get(9), user12, order10, false),
                        new Ticket(5, 13, screenings.get(9), user12, order10, false),
                        new Ticket(7, 7, screenings.get(9), user12, order10, false),
                        new Ticket(7, 18, screenings.get(9), user12, order10, false),
                        new Ticket(8, 13, screenings.get(9), user12, order10, true),
                        new Ticket(2, 20, screenings.get(9), user12, order10, true),
                        new Ticket(6, 2, screenings.get(9), user12, order10, false),
                        new Ticket(8, 12, screenings.get(9), user12, order10, false),
                        new Ticket(7, 13, screenings.get(9), user12, order10, false),
                        new Ticket(7, 6, screenings.get(9), user12, order10, false),
                        new Ticket(7, 3, screenings.get(9), user12, order10, true),
                        new Ticket(9, 20, screenings.get(9), user12, order10, true),
                        new Ticket(7, 9, screenings.get(9), user12, order10, true),
                        new Ticket(6, 12, screenings.get(9), user12, order10, true),
                        new Ticket(10, 20, screenings.get(9), user12, order10, true),
                        new Ticket(2, 19, screenings.get(9), user12, order10, false)
                );
                List<Ticket> tickets11 = List.of(
                        new Ticket(6, 13, screenings.get(50), user3, order11, false),
                        new Ticket(8, 17, screenings.get(50), user3, order11, true),
                        new Ticket(6, 13, screenings.get(50), user3, order11, true),
                        new Ticket(5, 13, screenings.get(50), user3, order11, true),
                        new Ticket(10, 13, screenings.get(50), user3, order11, true),
                        new Ticket(7, 13, screenings.get(50), user3, order11, true),
                        new Ticket(5, 19, screenings.get(50), user3, order11, true),
                        new Ticket(7, 21, screenings.get(50), user3, order11, true),
                        new Ticket(8, 18, screenings.get(50), user3, order11, false),
                        new Ticket(8, 13, screenings.get(50), user3, order11, false),
                        new Ticket(5, 17, screenings.get(50), user3, order11, false),
                        new Ticket(7, 21, screenings.get(50), user3, order11, false),
                        new Ticket(7, 20, screenings.get(50), user3, order11, false),
                        new Ticket(6, 10, screenings.get(50), user3, order11, true),
                        new Ticket(5, 13, screenings.get(50), user3, order11, false),
                        new Ticket(8, 10, screenings.get(50), user3, order11, true),
                        new Ticket(9, 21, screenings.get(50), user3, order11, false),
                        new Ticket(6, 14, screenings.get(50), user3, order11, false),
                        new Ticket(7, 18, screenings.get(50), user3, order11, false),
                        new Ticket(9, 13, screenings.get(50), user3, order11, false),
                        new Ticket(7, 16, screenings.get(50), user3, order11, true),
                        new Ticket(4, 14, screenings.get(50), user3, order11, true),
                        new Ticket(7, 15, screenings.get(50), user3, order11, true),
                        new Ticket(9, 17, screenings.get(50), user3, order11, false),
                        new Ticket(6, 6, screenings.get(50), user3, order11, true),
                        new Ticket(6, 16, screenings.get(50), user3, order11, true),
                        new Ticket(9, 16, screenings.get(50), user3, order11, true),
                        new Ticket(6, 21, screenings.get(50), user3, order11, false),
                        new Ticket(7, 18, screenings.get(50), user3, order11, true),
                        new Ticket(4, 19, screenings.get(50), user3, order11, false),
                        new Ticket(7, 16, screenings.get(50), user3, order11, false),
                        new Ticket(7, 9, screenings.get(50), user3, order11, false),
                        new Ticket(9, 22, screenings.get(50), user3, order11, true),
                        new Ticket(6, 17, screenings.get(50), user3, order11, false),
                        new Ticket(6, 18, screenings.get(50), user3, order11, true),
                        new Ticket(8, 16, screenings.get(50), user3, order11, true),
                        new Ticket(8, 21, screenings.get(50), user3, order11, false),
                        new Ticket(5, 8, screenings.get(50), user3, order11, true),
                        new Ticket(11, 22, screenings.get(50), user3, order11, true),
                        new Ticket(5, 8, screenings.get(50), user3, order11, false),
                        new Ticket(11, 19, screenings.get(50), user3, order11, true),
                        new Ticket(9, 16, screenings.get(50), user3, order11, false),
                        new Ticket(9, 19, screenings.get(50), user3, order11, false),
                        new Ticket(6, 24, screenings.get(50), user3, order11, false),
                        new Ticket(9, 17, screenings.get(50), user3, order11, true),
                        new Ticket(10, 9, screenings.get(50), user3, order11, false),
                        new Ticket(8, 17, screenings.get(50), user3, order11, false),
                        new Ticket(10, 14, screenings.get(50), user3, order11, true),
                        new Ticket(8, 15, screenings.get(50), user3, order11, true),
                        new Ticket(5, 17, screenings.get(50), user3, order11, true),
                        new Ticket(8, 16, screenings.get(50), user3, order11, true),
                        new Ticket(3, 13, screenings.get(50), user3, order11, false),
                        new Ticket(6, 19, screenings.get(50), user3, order11, true),
                        new Ticket(6, 19, screenings.get(50), user3, order11, true),
                        new Ticket(8, 12, screenings.get(50), user3, order11, true),
                        new Ticket(7, 14, screenings.get(50), user3, order11, false)
                );
                List<Ticket> tickets12 = List.of(
                        new Ticket(5, 11, screenings.get(50), user4, order12, true),
                        new Ticket(7, 8, screenings.get(50), user4, order12, true),
                        new Ticket(6, 18, screenings.get(50), user4, order12, false),
                        new Ticket(7, 19, screenings.get(50), user4, order12, true),
                        new Ticket(6, 17, screenings.get(50), user4, order12, false),
                        new Ticket(8, 14, screenings.get(50), user4, order12, false),
                        new Ticket(5, 12, screenings.get(50), user4, order12, false),
                        new Ticket(7, 11, screenings.get(50), user4, order12, false),
                        new Ticket(6, 15, screenings.get(50), user4, order12, true),
                        new Ticket(5, 12, screenings.get(50), user4, order12, true),
                        new Ticket(9, 16, screenings.get(50), user4, order12, false),
                        new Ticket(10, 17, screenings.get(50), user4, order12, false),
                        new Ticket(7, 17, screenings.get(50), user4, order12, false),
                        new Ticket(8, 14, screenings.get(50), user4, order12, false),
                        new Ticket(6, 15, screenings.get(50), user4, order12, true),
                        new Ticket(7, 14, screenings.get(50), user4, order12, false),
                        new Ticket(7, 17, screenings.get(50), user4, order12, true),
                        new Ticket(2, 16, screenings.get(50), user4, order12, true),
                        new Ticket(5, 11, screenings.get(50), user4, order12, true),
                        new Ticket(6, 16, screenings.get(50), user4, order12, false),
                        new Ticket(9, 19, screenings.get(50), user4, order12, false),
                        new Ticket(8, 20, screenings.get(50), user4, order12, false),
                        new Ticket(9, 17, screenings.get(50), user4, order12, false),
                        new Ticket(7, 19, screenings.get(50), user4, order12, true),
                        new Ticket(3, 15, screenings.get(50), user4, order12, true),
                        new Ticket(6, 20, screenings.get(50), user4, order12, true),
                        new Ticket(10, 10, screenings.get(50), user4, order12, true),
                        new Ticket(5, 18, screenings.get(50), user4, order12, true),
                        new Ticket(11, 10, screenings.get(50), user4, order12, true),
                        new Ticket(9, 14, screenings.get(50), user4, order12, false),
                        new Ticket(7, 6, screenings.get(50), user4, order12, false),
                        new Ticket(5, 14, screenings.get(50), user4, order12, false),
                        new Ticket(6, 12, screenings.get(50), user4, order12, false),
                        new Ticket(6, 11, screenings.get(50), user4, order12, true),
                        new Ticket(11, 17, screenings.get(50), user4, order12, false),
                        new Ticket(8, 21, screenings.get(50), user4, order12, true),
                        new Ticket(8, 22, screenings.get(50), user4, order12, false),
                        new Ticket(5, 19, screenings.get(50), user4, order12, false),
                        new Ticket(8, 15, screenings.get(50), user4, order12, true),
                        new Ticket(6, 15, screenings.get(50), user4, order12, false),
                        new Ticket(9, 10, screenings.get(50), user4, order12, true),
                        new Ticket(7, 16, screenings.get(50), user4, order12, false),
                        new Ticket(8, 18, screenings.get(50), user4, order12, true),
                        new Ticket(7, 23, screenings.get(50), user4, order12, false),
                        new Ticket(6, 12, screenings.get(50), user4, order12, false),
                        new Ticket(8, 12, screenings.get(50), user4, order12, true),
                        new Ticket(4, 9, screenings.get(50), user4, order12, true),
                        new Ticket(5, 10, screenings.get(50), user4, order12, true),
                        new Ticket(9, 27, screenings.get(50), user4, order12, false),
                        new Ticket(8, 10, screenings.get(50), user4, order12, true),
                        new Ticket(6, 18, screenings.get(50), user4, order12, true),
                        new Ticket(7, 13, screenings.get(50), user4, order12, true),
                        new Ticket(5, 10, screenings.get(50), user4, order12, false),
                        new Ticket(7, 14, screenings.get(50), user4, order12, true),
                        new Ticket(5, 25, screenings.get(50), user4, order12, true),
                        new Ticket(8, 12, screenings.get(50), user4, order12, true),
                        new Ticket(3, 10, screenings.get(50), user4, order12, true),
                        new Ticket(4, 11, screenings.get(50), user4, order12, true),
                        new Ticket(8, 7, screenings.get(50), user4, order12, false),
                        new Ticket(7, 15, screenings.get(50), user4, order12, true),
                        new Ticket(12, 16, screenings.get(50), user4, order12, false),
                        new Ticket(6, 27, screenings.get(50), user4, order12, false),
                        new Ticket(6, 13, screenings.get(50), user4, order12, false),
                        new Ticket(8, 15, screenings.get(50), user4, order12, false),
                        new Ticket(10, 18, screenings.get(50), user4, order12, true)
                );
                List<Ticket> tickets13 = List.of(
                        new Ticket(5, 23, screenings.get(50), user5, order13, true),
                        new Ticket(5, 30, screenings.get(50), user5, order13, true),
                        new Ticket(4, 18, screenings.get(50), user5, order13, true),
                        new Ticket(8, 26, screenings.get(50), user5, order13, false),
                        new Ticket(4, 14, screenings.get(50), user5, order13, false),
                        new Ticket(10, 28, screenings.get(50), user5, order13, true),
                        new Ticket(6, 14, screenings.get(50), user5, order13, false),
                        new Ticket(8, 16, screenings.get(50), user5, order13, false),
                        new Ticket(5, 17, screenings.get(50), user5, order13, true),
                        new Ticket(6, 14, screenings.get(50), user5, order13, true),
                        new Ticket(7, 13, screenings.get(50), user5, order13, true),
                        new Ticket(10, 14, screenings.get(50), user5, order13, false),
                        new Ticket(6, 17, screenings.get(50), user5, order13, false),
                        new Ticket(5, 14, screenings.get(50), user5, order13, false),
                        new Ticket(6, 7, screenings.get(50), user5, order13, true),
                        new Ticket(8, 17, screenings.get(50), user5, order13, false),
                        new Ticket(6, 18, screenings.get(50), user5, order13, true),
                        new Ticket(9, 12, screenings.get(50), user5, order13, false),
                        new Ticket(8, 11, screenings.get(50), user5, order13, false),
                        new Ticket(6, 15, screenings.get(50), user5, order13, true),
                        new Ticket(5, 16, screenings.get(50), user5, order13, false),
                        new Ticket(9, 16, screenings.get(50), user5, order13, false),
                        new Ticket(8, 13, screenings.get(50), user5, order13, true),
                        new Ticket(8, 19, screenings.get(50), user5, order13, true),
                        new Ticket(7, 20, screenings.get(50), user5, order13, false),
                        new Ticket(7, 20, screenings.get(50), user5, order13, false),
                        new Ticket(8, 19, screenings.get(50), user5, order13, false),
                        new Ticket(5, 13, screenings.get(50), user5, order13, true),
                        new Ticket(3, 13, screenings.get(50), user5, order13, true),
                        new Ticket(6, 19, screenings.get(50), user5, order13, true),
                        new Ticket(7, 23, screenings.get(50), user5, order13, false),
                        new Ticket(8, 15, screenings.get(50), user5, order13, true),
                        new Ticket(9, 19, screenings.get(50), user5, order13, false),
                        new Ticket(6, 18, screenings.get(50), user5, order13, true),
                        new Ticket(7, 18, screenings.get(50), user5, order13, false),
                        new Ticket(6, 9, screenings.get(50), user5, order13, true),
                        new Ticket(10, 8, screenings.get(50), user5, order13, false),
                        new Ticket(8, 19, screenings.get(50), user5, order13, true),
                        new Ticket(5, 7, screenings.get(50), user5, order13, false),
                        new Ticket(6, 21, screenings.get(50), user5, order13, true),
                        new Ticket(6, 21, screenings.get(50), user5, order13, true),
                        new Ticket(10, 14, screenings.get(50), user5, order13, false),
                        new Ticket(8, 16, screenings.get(50), user5, order13, true),
                        new Ticket(4, 17, screenings.get(50), user5, order13, false),
                        new Ticket(6, 13, screenings.get(50), user5, order13, false),
                        new Ticket(5, 9, screenings.get(50), user5, order13, true),
                        new Ticket(8, 13, screenings.get(50), user5, order13, false),
                        new Ticket(6, 19, screenings.get(50), user5, order13, false),
                        new Ticket(8, 14, screenings.get(50), user5, order13, true),
                        new Ticket(10, 11, screenings.get(50), user5, order13, false),
                        new Ticket(10, 14, screenings.get(50), user5, order13, true),
                        new Ticket(8, 13, screenings.get(50), user5, order13, false),
                        new Ticket(5, 18, screenings.get(50), user5, order13, false),
                        new Ticket(8, 13, screenings.get(50), user5, order13, true),
                        new Ticket(6, 21, screenings.get(50), user5, order13, false),
                        new Ticket(11, 22, screenings.get(50), user5, order13, false),
                        new Ticket(4, 12, screenings.get(50), user5, order13, true),
                        new Ticket(5, 16, screenings.get(50), user5, order13, true),
                        new Ticket(8, 7, screenings.get(50), user5, order13, true),
                        new Ticket(10, 26, screenings.get(50), user5, order13, false),
                        new Ticket(4, 13, screenings.get(50), user5, order13, false),
                        new Ticket(8, 16, screenings.get(50), user5, order13, false),
                        new Ticket(6, 23, screenings.get(50), user5, order13, false)
                );
                List<Ticket> tickets14 = List.of(
                        new Ticket(3, 11, screenings.get(51), user6, order14, false),
                        new Ticket(1, 1, screenings.get(51), user6, order14, false),
                        new Ticket(5, 19, screenings.get(51), user6, order14, true),
                        new Ticket(7, 9, screenings.get(51), user6, order14, true),
                        new Ticket(6, 10, screenings.get(51), user6, order14, false),
                        new Ticket(4, 14, screenings.get(51), user6, order14, true),
                        new Ticket(7, 6, screenings.get(51), user6, order14, true),
                        new Ticket(5, 11, screenings.get(51), user6, order14, false),
                        new Ticket(4, 11, screenings.get(51), user6, order14, true),
                        new Ticket(4, 17, screenings.get(51), user6, order14, false),
                        new Ticket(4, 11, screenings.get(51), user6, order14, false),
                        new Ticket(4, 5, screenings.get(51), user6, order14, true),
                        new Ticket(6, 9, screenings.get(51), user6, order14, true),
                        new Ticket(2, 7, screenings.get(51), user6, order14, true),
                        new Ticket(5, 3, screenings.get(51), user6, order14, true),
                        new Ticket(4, 10, screenings.get(51), user6, order14, false),
                        new Ticket(6, 10, screenings.get(51), user6, order14, false),
                        new Ticket(9, 19, screenings.get(51), user6, order14, true),
                        new Ticket(5, 11, screenings.get(51), user6, order14, false),
                        new Ticket(2, 4, screenings.get(51), user6, order14, true),
                        new Ticket(4, 19, screenings.get(51), user6, order14, false),
                        new Ticket(3, 7, screenings.get(51), user6, order14, false),
                        new Ticket(4, 9, screenings.get(51), user6, order14, true),
                        new Ticket(6, 4, screenings.get(51), user6, order14, false),
                        new Ticket(6, 8, screenings.get(51), user6, order14, false),
                        new Ticket(5, 19, screenings.get(51), user6, order14, true),
                        new Ticket(1, 14, screenings.get(51), user6, order14, true),
                        new Ticket(3, 12, screenings.get(51), user6, order14, true),
                        new Ticket(7, 11, screenings.get(51), user6, order14, true),
                        new Ticket(3, 16, screenings.get(51), user6, order14, false),
                        new Ticket(1, 8, screenings.get(51), user6, order14, true),
                        new Ticket(5, 6, screenings.get(51), user6, order14, false),
                        new Ticket(2, 2, screenings.get(51), user6, order14, false),
                        new Ticket(3, 9, screenings.get(51), user6, order14, false),
                        new Ticket(6, 2, screenings.get(51), user6, order14, false),
                        new Ticket(10, 12, screenings.get(51), user6, order14, false),
                        new Ticket(5, 14, screenings.get(51), user6, order14, false),
                        new Ticket(7, 6, screenings.get(51), user6, order14, false),
                        new Ticket(4, 18, screenings.get(51), user6, order14, false),
                        new Ticket(5, 14, screenings.get(51), user6, order14, true),
                        new Ticket(6, 7, screenings.get(51), user6, order14, true),
                        new Ticket(5, 10, screenings.get(51), user6, order14, false),
                        new Ticket(7, 17, screenings.get(51), user6, order14, false),
                        new Ticket(6, 10, screenings.get(51), user6, order14, true),
                        new Ticket(4, 13, screenings.get(51), user6, order14, false),
                        new Ticket(6, 16, screenings.get(51), user6, order14, false),
                        new Ticket(3, 8, screenings.get(51), user6, order14, true),
                        new Ticket(6, 8, screenings.get(51), user6, order14, false),
                        new Ticket(6, 10, screenings.get(51), user6, order14, false),
                        new Ticket(6, 11, screenings.get(51), user6, order14, true),
                        new Ticket(7, 14, screenings.get(51), user6, order14, false),
                        new Ticket(8, 18, screenings.get(51), user6, order14, false),
                        new Ticket(4, 18, screenings.get(51), user6, order14, true),
                        new Ticket(5, 7, screenings.get(51), user6, order14, true),
                        new Ticket(5, 8, screenings.get(51), user6, order14, false),
                        new Ticket(8, 11, screenings.get(51), user6, order14, true),
                        new Ticket(7, 12, screenings.get(51), user6, order14, false),
                        new Ticket(7, 12, screenings.get(51), user6, order14, false)
                );
                List<Ticket> tickets15 = List.of(
                        new Ticket(1, 17, screenings.get(51), user7, order15, true),
                        new Ticket(9, 14, screenings.get(51), user7, order15, false),
                        new Ticket(3, 12, screenings.get(51), user7, order15, true),
                        new Ticket(4, 11, screenings.get(51), user7, order15, false),
                        new Ticket(4, 4, screenings.get(51), user7, order15, true),
                        new Ticket(6, 11, screenings.get(51), user7, order15, true),
                        new Ticket(5, 16, screenings.get(51), user7, order15, true),
                        new Ticket(3, 9, screenings.get(51), user7, order15, false),
                        new Ticket(7, 17, screenings.get(51), user7, order15, false),
                        new Ticket(3, 8, screenings.get(51), user7, order15, false),
                        new Ticket(7, 10, screenings.get(51), user7, order15, true),
                        new Ticket(5, 9, screenings.get(51), user7, order15, false),
                        new Ticket(3, 5, screenings.get(51), user7, order15, true),
                        new Ticket(8, 1, screenings.get(51), user7, order15, true),
                        new Ticket(5, 14, screenings.get(51), user7, order15, false),
                        new Ticket(5, 14, screenings.get(51), user7, order15, true),
                        new Ticket(2, 12, screenings.get(51), user7, order15, true),
                        new Ticket(4, 8, screenings.get(51), user7, order15, true),
                        new Ticket(7, 10, screenings.get(51), user7, order15, true),
                        new Ticket(3, 9, screenings.get(51), user7, order15, false),
                        new Ticket(4, 1, screenings.get(51), user7, order15, false),
                        new Ticket(6, 4, screenings.get(51), user7, order15, false),
                        new Ticket(9, 20, screenings.get(51), user7, order15, true),
                        new Ticket(6, 19, screenings.get(51), user7, order15, true),
                        new Ticket(6, 13, screenings.get(51), user7, order15, true),
                        new Ticket(6, 7, screenings.get(51), user7, order15, false),
                        new Ticket(5, 15, screenings.get(51), user7, order15, false),
                        new Ticket(2, 12, screenings.get(51), user7, order15, true),
                        new Ticket(7, 2, screenings.get(51), user7, order15, false),
                        new Ticket(3, 6, screenings.get(51), user7, order15, true),
                        new Ticket(7, 4, screenings.get(51), user7, order15, true),
                        new Ticket(4, 6, screenings.get(51), user7, order15, true),
                        new Ticket(5, 2, screenings.get(51), user7, order15, true),
                        new Ticket(4, 11, screenings.get(51), user7, order15, true),
                        new Ticket(6, 11, screenings.get(51), user7, order15, true),
                        new Ticket(6, 11, screenings.get(51), user7, order15, false),
                        new Ticket(8, 5, screenings.get(51), user7, order15, false),
                        new Ticket(3, 12, screenings.get(51), user7, order15, true),
                        new Ticket(8, 8, screenings.get(51), user7, order15, true),
                        new Ticket(5, 8, screenings.get(51), user7, order15, false),
                        new Ticket(2, 1, screenings.get(51), user7, order15, true),
                        new Ticket(6, 15, screenings.get(51), user7, order15, true),
                        new Ticket(5, 5, screenings.get(51), user7, order15, true),
                        new Ticket(5, 10, screenings.get(51), user7, order15, false),
                        new Ticket(5, 12, screenings.get(51), user7, order15, false),
                        new Ticket(5, 17, screenings.get(51), user7, order15, false),
                        new Ticket(6, 7, screenings.get(51), user7, order15, true),
                        new Ticket(5, 7, screenings.get(51), user7, order15, false),
                        new Ticket(7, 7, screenings.get(51), user7, order15, false),
                        new Ticket(1, 20, screenings.get(51), user7, order15, true),
                        new Ticket(8, 8, screenings.get(51), user7, order15, false),
                        new Ticket(6, 9, screenings.get(51), user7, order15, false),
                        new Ticket(3, 8, screenings.get(51), user7, order15, true),
                        new Ticket(6, 13, screenings.get(51), user7, order15, true),
                        new Ticket(4, 5, screenings.get(51), user7, order15, false),
                        new Ticket(5, 7, screenings.get(51), user7, order15, true),
                        new Ticket(9, 12, screenings.get(51), user7, order15, true),
                        new Ticket(4, 9, screenings.get(51), user7, order15, false),
                        new Ticket(6, 10, screenings.get(51), user7, order15, true),
                        new Ticket(7, 7, screenings.get(51), user7, order15, true),
                        new Ticket(1, 12, screenings.get(51), user7, order15, true),
                        new Ticket(5, 11, screenings.get(51), user7, order15, true),
                        new Ticket(1, 3, screenings.get(51), user7, order15, true),
                        new Ticket(5, 8, screenings.get(51), user7, order15, true),
                        new Ticket(6, 4, screenings.get(51), user7, order15, true),
                        new Ticket(5, 14, screenings.get(51), user7, order15, false),
                        new Ticket(7, 3, screenings.get(51), user7, order15, true),
                        new Ticket(5, 5, screenings.get(51), user7, order15, true),
                        new Ticket(3, 11, screenings.get(51), user7, order15, true),
                        new Ticket(5, 13, screenings.get(51), user7, order15, false),
                        new Ticket(8, 12, screenings.get(51), user7, order15, false),
                        new Ticket(4, 20, screenings.get(51), user7, order15, false),
                        new Ticket(6, 8, screenings.get(51), user7, order15, false),
                        new Ticket(4, 9, screenings.get(51), user7, order15, true),
                        new Ticket(6, 14, screenings.get(51), user7, order15, false),
                        new Ticket(5, 11, screenings.get(51), user7, order15, false),
                        new Ticket(4, 12, screenings.get(51), user7, order15, true),
                        new Ticket(7, 2, screenings.get(51), user7, order15, false),
                        new Ticket(5, 17, screenings.get(51), user7, order15, false),
                        new Ticket(7, 13, screenings.get(51), user7, order15, false),
                        new Ticket(5, 1, screenings.get(51), user7, order15, true),
                        new Ticket(5, 5, screenings.get(51), user7, order15, false),
                        new Ticket(3, 14, screenings.get(51), user7, order15, true),
                        new Ticket(8, 9, screenings.get(51), user7, order15, false),
                        new Ticket(6, 15, screenings.get(51), user7, order15, false),
                        new Ticket(6, 13, screenings.get(51), user7, order15, false),
                        new Ticket(5, 8, screenings.get(51), user7, order15, true),
                        new Ticket(5, 14, screenings.get(51), user7, order15, true),
                        new Ticket(6, 6, screenings.get(51), user7, order15, false),
                        new Ticket(6, 16, screenings.get(51), user7, order15, true),
                        new Ticket(5, 12, screenings.get(51), user7, order15, true)
                );
                List<Ticket> tickets16 = List.of(
                        new Ticket(8, 15, screenings.get(76), user8, order16, false),
                        new Ticket(7, 15, screenings.get(76), user8, order16, false),
                        new Ticket(1, 11, screenings.get(76), user8, order16, false),
                        new Ticket(9, 6, screenings.get(76), user8, order16, true),
                        new Ticket(11, 8, screenings.get(76), user8, order16, false),
                        new Ticket(12, 9, screenings.get(76), user8, order16, false),
                        new Ticket(7, 9, screenings.get(76), user8, order16, false),
                        new Ticket(7, 20, screenings.get(76), user8, order16, false),
                        new Ticket(8, 12, screenings.get(76), user8, order16, true),
                        new Ticket(8, 8, screenings.get(76), user8, order16, false),
                        new Ticket(6, 13, screenings.get(76), user8, order16, false),
                        new Ticket(9, 15, screenings.get(76), user8, order16, true),
                        new Ticket(11, 12, screenings.get(76), user8, order16, false),
                        new Ticket(7, 12, screenings.get(76), user8, order16, false),
                        new Ticket(10, 6, screenings.get(76), user8, order16, false),
                        new Ticket(8, 15, screenings.get(76), user8, order16, false),
                        new Ticket(5, 10, screenings.get(76), user8, order16, false),
                        new Ticket(6, 11, screenings.get(76), user8, order16, true),
                        new Ticket(5, 7, screenings.get(76), user8, order16, true),
                        new Ticket(7, 15, screenings.get(76), user8, order16, true),
                        new Ticket(8, 10, screenings.get(76), user8, order16, true),
                        new Ticket(9, 19, screenings.get(76), user8, order16, true),
                        new Ticket(8, 8, screenings.get(76), user8, order16, true),
                        new Ticket(11, 13, screenings.get(76), user8, order16, true),
                        new Ticket(10, 6, screenings.get(76), user8, order16, false),
                        new Ticket(5, 16, screenings.get(76), user8, order16, false),
                        new Ticket(8, 12, screenings.get(76), user8, order16, false),
                        new Ticket(8, 10, screenings.get(76), user8, order16, false),
                        new Ticket(9, 17, screenings.get(76), user8, order16, false),
                        new Ticket(8, 15, screenings.get(76), user8, order16, true),
                        new Ticket(8, 10, screenings.get(76), user8, order16, true),
                        new Ticket(8, 7, screenings.get(76), user8, order16, false),
                        new Ticket(9, 9, screenings.get(76), user8, order16, false),
                        new Ticket(8, 9, screenings.get(76), user8, order16, false),
                        new Ticket(9, 12, screenings.get(76), user8, order16, true),
                        new Ticket(5, 18, screenings.get(76), user8, order16, true),
                        new Ticket(8, 11, screenings.get(76), user8, order16, false),
                        new Ticket(6, 12, screenings.get(76), user8, order16, false),
                        new Ticket(7, 9, screenings.get(76), user8, order16, true),
                        new Ticket(8, 12, screenings.get(76), user8, order16, true),
                        new Ticket(5, 14, screenings.get(76), user8, order16, false),
                        new Ticket(7, 13, screenings.get(76), user8, order16, true),
                        new Ticket(5, 15, screenings.get(76), user8, order16, false),
                        new Ticket(10, 15, screenings.get(76), user8, order16, false),
                        new Ticket(5, 15, screenings.get(76), user8, order16, true),
                        new Ticket(8, 13, screenings.get(76), user8, order16, false),
                        new Ticket(6, 25, screenings.get(76), user8, order16, true)
                );
                List<Ticket> tickets17 = List.of(
                        new Ticket(7, 10, screenings.get(76), user9, order17, false),
                        new Ticket(6, 15, screenings.get(76), user9, order17, false),
                        new Ticket(9, 16, screenings.get(76), user9, order17, false),
                        new Ticket(8, 12, screenings.get(76), user9, order17, true),
                        new Ticket(10, 20, screenings.get(76), user9, order17, false),
                        new Ticket(6, 13, screenings.get(76), user9, order17, false),
                        new Ticket(8, 18, screenings.get(76), user9, order17, false),
                        new Ticket(9, 9, screenings.get(76), user9, order17, true),
                        new Ticket(7, 8, screenings.get(76), user9, order17, true),
                        new Ticket(7, 2, screenings.get(76), user9, order17, false),
                        new Ticket(6, 11, screenings.get(76), user9, order17, false),
                        new Ticket(5, 8, screenings.get(76), user9, order17, false),
                        new Ticket(8, 5, screenings.get(76), user9, order17, true),
                        new Ticket(9, 6, screenings.get(76), user9, order17, true),
                        new Ticket(7, 17, screenings.get(76), user9, order17, true),
                        new Ticket(8, 14, screenings.get(76), user9, order17, false),
                        new Ticket(9, 10, screenings.get(76), user9, order17, true),
                        new Ticket(7, 7, screenings.get(76), user9, order17, false),
                        new Ticket(7, 17, screenings.get(76), user9, order17, true),
                        new Ticket(8, 7, screenings.get(76), user9, order17, false),
                        new Ticket(8, 13, screenings.get(76), user9, order17, false),
                        new Ticket(5, 14, screenings.get(76), user9, order17, false),
                        new Ticket(9, 16, screenings.get(76), user9, order17, false),
                        new Ticket(8, 9, screenings.get(76), user9, order17, false),
                        new Ticket(7, 6, screenings.get(76), user9, order17, false),
                        new Ticket(7, 16, screenings.get(76), user9, order17, true),
                        new Ticket(7, 4, screenings.get(76), user9, order17, true),
                        new Ticket(7, 6, screenings.get(76), user9, order17, false),
                        new Ticket(5, 14, screenings.get(76), user9, order17, false),
                        new Ticket(9, 17, screenings.get(76), user9, order17, false),
                        new Ticket(6, 11, screenings.get(76), user9, order17, false),
                        new Ticket(9, 9, screenings.get(76), user9, order17, true),
                        new Ticket(12, 15, screenings.get(76), user9, order17, false),
                        new Ticket(7, 6, screenings.get(76), user9, order17, true),
                        new Ticket(7, 20, screenings.get(76), user9, order17, false),
                        new Ticket(4, 18, screenings.get(76), user9, order17, true),
                        new Ticket(12, 12, screenings.get(76), user9, order17, false),
                        new Ticket(8, 11, screenings.get(76), user9, order17, false),
                        new Ticket(9, 13, screenings.get(76), user9, order17, false),
                        new Ticket(6, 10, screenings.get(76), user9, order17, false),
                        new Ticket(10, 19, screenings.get(76), user9, order17, false),
                        new Ticket(6, 16, screenings.get(76), user9, order17, true),
                        new Ticket(9, 9, screenings.get(76), user9, order17, false),
                        new Ticket(11, 20, screenings.get(76), user9, order17, false),
                        new Ticket(8, 16, screenings.get(76), user9, order17, true),
                        new Ticket(8, 16, screenings.get(76), user9, order17, true),
                        new Ticket(8, 5, screenings.get(76), user9, order17, false),
                        new Ticket(5, 15, screenings.get(76), user9, order17, true),
                        new Ticket(11, 20, screenings.get(76), user9, order17, true),
                        new Ticket(5, 18, screenings.get(76), user9, order17, true),
                        new Ticket(10, 10, screenings.get(76), user9, order17, true),
                        new Ticket(7, 10, screenings.get(76), user9, order17, true),
                        new Ticket(9, 20, screenings.get(76), user9, order17, true),
                        new Ticket(10, 17, screenings.get(76), user9, order17, true),
                        new Ticket(7, 16, screenings.get(76), user9, order17, false),
                        new Ticket(6, 11, screenings.get(76), user9, order17, false),
                        new Ticket(7, 14, screenings.get(76), user9, order17, false),
                        new Ticket(7, 7, screenings.get(76), user9, order17, false),
                        new Ticket(7, 10, screenings.get(76), user9, order17, false),
                        new Ticket(8, 11, screenings.get(76), user9, order17, false),
                        new Ticket(6, 8, screenings.get(76), user9, order17, true),
                        new Ticket(5, 7, screenings.get(76), user9, order17, true),
                        new Ticket(8, 15, screenings.get(76), user9, order17, false),
                        new Ticket(7, 14, screenings.get(76), user9, order17, true),
                        new Ticket(8, 6, screenings.get(76), user9, order17, false),
                        new Ticket(6, 21, screenings.get(76), user9, order17, false),
                        new Ticket(8, 17, screenings.get(76), user9, order17, true),
                        new Ticket(9, 13, screenings.get(76), user9, order17, true),
                        new Ticket(11, 9, screenings.get(76), user9, order17, false),
                        new Ticket(6, 15, screenings.get(76), user9, order17, true),
                        new Ticket(10, 20, screenings.get(76), user9, order17, true),
                        new Ticket(9, 2, screenings.get(76), user9, order17, true),
                        new Ticket(8, 10, screenings.get(76), user9, order17, true),
                        new Ticket(7, 4, screenings.get(76), user9, order17, false),
                        new Ticket(7, 16, screenings.get(76), user9, order17, true),
                        new Ticket(12, 15, screenings.get(76), user9, order17, true),
                        new Ticket(8, 13, screenings.get(76), user9, order17, false),
                        new Ticket(5, 17, screenings.get(76), user9, order17, false),
                        new Ticket(6, 18, screenings.get(76), user9, order17, true),
                        new Ticket(8, 10, screenings.get(76), user9, order17, true),
                        new Ticket(6, 19, screenings.get(76), user9, order17, true),
                        new Ticket(10, 12, screenings.get(76), user9, order17, true),
                        new Ticket(8, 11, screenings.get(76), user9, order17, false),
                        new Ticket(8, 1, screenings.get(76), user9, order17, false),
                        new Ticket(7, 5, screenings.get(76), user9, order17, false)
                );
                List<Ticket> tickets18 = List.of(
                        new Ticket(5, 7, screenings.get(78), user10, order18, true),
                        new Ticket(4, 4, screenings.get(78), user10, order18, true),
                        new Ticket(7, 15, screenings.get(78), user10, order18, true),
                        new Ticket(5, 14, screenings.get(78), user10, order18, false),
                        new Ticket(5, 11, screenings.get(78), user10, order18, true),
                        new Ticket(7, 14, screenings.get(78), user10, order18, false),
                        new Ticket(3, 10, screenings.get(78), user10, order18, false),
                        new Ticket(4, 6, screenings.get(78), user10, order18, false),
                        new Ticket(3, 14, screenings.get(78), user10, order18, false),
                        new Ticket(3, 10, screenings.get(78), user10, order18, false),
                        new Ticket(5, 16, screenings.get(78), user10, order18, true),
                        new Ticket(5, 9, screenings.get(78), user10, order18, false),
                        new Ticket(5, 6, screenings.get(78), user10, order18, true),
                        new Ticket(2, 5, screenings.get(78), user10, order18, false),
                        new Ticket(6, 8, screenings.get(78), user10, order18, false),
                        new Ticket(3, 5, screenings.get(78), user10, order18, false),
                        new Ticket(6, 8, screenings.get(78), user10, order18, false),
                        new Ticket(6, 3, screenings.get(78), user10, order18, false),
                        new Ticket(3, 13, screenings.get(78), user10, order18, true),
                        new Ticket(3, 13, screenings.get(78), user10, order18, false),
                        new Ticket(6, 11, screenings.get(78), user10, order18, false),
                        new Ticket(8, 9, screenings.get(78), user10, order18, false),
                        new Ticket(6, 11, screenings.get(78), user10, order18, true),
                        new Ticket(1, 6, screenings.get(78), user10, order18, false),
                        new Ticket(1, 18, screenings.get(78), user10, order18, false),
                        new Ticket(4, 7, screenings.get(78), user10, order18, true),
                        new Ticket(4, 8, screenings.get(78), user10, order18, true),
                        new Ticket(3, 14, screenings.get(78), user10, order18, true),
                        new Ticket(6, 7, screenings.get(78), user10, order18, true),
                        new Ticket(4, 5, screenings.get(78), user10, order18, false),
                        new Ticket(3, 3, screenings.get(78), user10, order18, false),
                        new Ticket(3, 1, screenings.get(78), user10, order18, true),
                        new Ticket(3, 8, screenings.get(78), user10, order18, true),
                        new Ticket(7, 10, screenings.get(78), user10, order18, false),
                        new Ticket(4, 11, screenings.get(78), user10, order18, true),
                        new Ticket(5, 15, screenings.get(78), user10, order18, true),
                        new Ticket(3, 8, screenings.get(78), user10, order18, true),
                        new Ticket(3, 1, screenings.get(78), user10, order18, true),
                        new Ticket(2, 6, screenings.get(78), user10, order18, true),
                        new Ticket(1, 11, screenings.get(78), user10, order18, false),
                        new Ticket(5, 12, screenings.get(78), user10, order18, true),
                        new Ticket(1, 9, screenings.get(78), user10, order18, true),
                        new Ticket(2, 5, screenings.get(78), user10, order18, true),
                        new Ticket(4, 17, screenings.get(78), user10, order18, false),
                        new Ticket(8, 4, screenings.get(78), user10, order18, true),
                        new Ticket(4, 1, screenings.get(78), user10, order18, false),
                        new Ticket(5, 18, screenings.get(78), user10, order18, true),
                        new Ticket(7, 9, screenings.get(78), user10, order18, false),
                        new Ticket(3, 18, screenings.get(78), user10, order18, true),
                        new Ticket(3, 7, screenings.get(78), user10, order18, true),
                        new Ticket(5, 7, screenings.get(78), user10, order18, true),
                        new Ticket(7, 4, screenings.get(78), user10, order18, false),
                        new Ticket(2, 11, screenings.get(78), user10, order18, true),
                        new Ticket(8, 16, screenings.get(78), user10, order18, false),
                        new Ticket(4, 9, screenings.get(78), user10, order18, false),
                        new Ticket(3, 9, screenings.get(78), user10, order18, true),
                        new Ticket(2, 6, screenings.get(78), user10, order18, false),
                        new Ticket(2, 11, screenings.get(78), user10, order18, true),
                        new Ticket(8, 7, screenings.get(78), user10, order18, true),
                        new Ticket(1, 2, screenings.get(78), user10, order18, true),
                        new Ticket(1, 10, screenings.get(78), user10, order18, false),
                        new Ticket(2, 15, screenings.get(78), user10, order18, false),
                        new Ticket(1, 13, screenings.get(78), user10, order18, false),
                        new Ticket(4, 11, screenings.get(78), user10, order18, false),
                        new Ticket(5, 9, screenings.get(78), user10, order18, true)
                );
                List<Ticket> tickets19 = List.of(
                        new Ticket(5, 8, screenings.get(78), user11, order19, true),
                        new Ticket(1, 18, screenings.get(78), user11, order19, true),
                        new Ticket(1, 9, screenings.get(78), user11, order19, true),
                        new Ticket(2, 9, screenings.get(78), user11, order19, true),
                        new Ticket(4, 1, screenings.get(78), user11, order19, true),
                        new Ticket(4, 5, screenings.get(78), user11, order19, true),
                        new Ticket(7, 10, screenings.get(78), user11, order19, false),
                        new Ticket(3, 13, screenings.get(78), user11, order19, true),
                        new Ticket(3, 8, screenings.get(78), user11, order19, true),
                        new Ticket(2, 16, screenings.get(78), user11, order19, true),
                        new Ticket(4, 7, screenings.get(78), user11, order19, true),
                        new Ticket(5, 16, screenings.get(78), user11, order19, false),
                        new Ticket(6, 9, screenings.get(78), user11, order19, false),
                        new Ticket(6, 2, screenings.get(78), user11, order19, false),
                        new Ticket(4, 5, screenings.get(78), user11, order19, true),
                        new Ticket(4, 10, screenings.get(78), user11, order19, false),
                        new Ticket(4, 10, screenings.get(78), user11, order19, true),
                        new Ticket(4, 7, screenings.get(78), user11, order19, true),
                        new Ticket(2, 7, screenings.get(78), user11, order19, true),
                        new Ticket(5, 5, screenings.get(78), user11, order19, true),
                        new Ticket(3, 7, screenings.get(78), user11, order19, false),
                        new Ticket(2, 1, screenings.get(78), user11, order19, true),
                        new Ticket(6, 11, screenings.get(78), user11, order19, true),
                        new Ticket(2, 7, screenings.get(78), user11, order19, true),
                        new Ticket(3, 16, screenings.get(78), user11, order19, true),
                        new Ticket(4, 13, screenings.get(78), user11, order19, true),
                        new Ticket(7, 11, screenings.get(78), user11, order19, false),
                        new Ticket(8, 12, screenings.get(78), user11, order19, false),
                        new Ticket(5, 14, screenings.get(78), user11, order19, true),
                        new Ticket(2, 8, screenings.get(78), user11, order19, true),
                        new Ticket(5, 4, screenings.get(78), user11, order19, false),
                        new Ticket(6, 1, screenings.get(78), user11, order19, true),
                        new Ticket(8, 15, screenings.get(78), user11, order19, false),
                        new Ticket(4, 11, screenings.get(78), user11, order19, true),
                        new Ticket(1, 14, screenings.get(78), user11, order19, false),
                        new Ticket(8, 9, screenings.get(78), user11, order19, true),
                        new Ticket(4, 2, screenings.get(78), user11, order19, true),
                        new Ticket(7, 9, screenings.get(78), user11, order19, false),
                        new Ticket(5, 5, screenings.get(78), user11, order19, false),
                        new Ticket(3, 10, screenings.get(78), user11, order19, true),
                        new Ticket(3, 10, screenings.get(78), user11, order19, true),
                        new Ticket(4, 11, screenings.get(78), user11, order19, true),
                        new Ticket(4, 10, screenings.get(78), user11, order19, false),
                        new Ticket(6, 5, screenings.get(78), user11, order19, false),
                        new Ticket(2, 13, screenings.get(78), user11, order19, true),
                        new Ticket(4, 14, screenings.get(78), user11, order19, false),
                        new Ticket(3, 18, screenings.get(78), user11, order19, true),
                        new Ticket(3, 9, screenings.get(78), user11, order19, false),
                        new Ticket(3, 4, screenings.get(78), user11, order19, true)
                );
                List<Ticket> tickets20 = List.of(
                        new Ticket(10, 19, screenings.get(76), user12, order20, false),
                        new Ticket(7, 20, screenings.get(76), user12, order20, false),
                        new Ticket(9, 15, screenings.get(76), user12, order20, true),
                        new Ticket(7, 9, screenings.get(76), user12, order20, false),
                        new Ticket(9, 18, screenings.get(76), user12, order20, true),
                        new Ticket(9, 7, screenings.get(76), user12, order20, false),
                        new Ticket(8, 4, screenings.get(76), user12, order20, true),
                        new Ticket(7, 16, screenings.get(76), user12, order20, true),
                        new Ticket(6, 10, screenings.get(76), user12, order20, false),
                        new Ticket(5, 7, screenings.get(76), user12, order20, true),
                        new Ticket(4, 11, screenings.get(76), user12, order20, true),
                        new Ticket(7, 10, screenings.get(76), user12, order20, true),
                        new Ticket(10, 10, screenings.get(76), user12, order20, false),
                        new Ticket(5, 20, screenings.get(76), user12, order20, true),
                        new Ticket(7, 13, screenings.get(76), user12, order20, false),
                        new Ticket(8, 9, screenings.get(76), user12, order20, false),
                        new Ticket(7, 3, screenings.get(76), user12, order20, false),
                        new Ticket(9, 15, screenings.get(76), user12, order20, true),
                        new Ticket(8, 11, screenings.get(76), user12, order20, true),
                        new Ticket(8, 8, screenings.get(76), user12, order20, true),
                        new Ticket(7, 10, screenings.get(76), user12, order20, false),
                        new Ticket(6, 17, screenings.get(76), user12, order20, true),
                        new Ticket(6, 9, screenings.get(76), user12, order20, false),
                        new Ticket(9, 11, screenings.get(76), user12, order20, true),
                        new Ticket(11, 15, screenings.get(76), user12, order20, true),
                        new Ticket(8, 13, screenings.get(76), user12, order20, true),
                        new Ticket(12, 19, screenings.get(76), user12, order20, false),
                        new Ticket(6, 14, screenings.get(76), user12, order20, true),
                        new Ticket(9, 10, screenings.get(76), user12, order20, true),
                        new Ticket(7, 16, screenings.get(76), user12, order20, true),
                        new Ticket(8, 19, screenings.get(76), user12, order20, true),
                        new Ticket(9, 2, screenings.get(76), user12, order20, false),
                        new Ticket(9, 12, screenings.get(76), user12, order20, true),
                        new Ticket(8, 7, screenings.get(76), user12, order20, false),
                        new Ticket(7, 13, screenings.get(76), user12, order20, false),
                        new Ticket(8, 25, screenings.get(76), user12, order20, true),
                        new Ticket(7, 15, screenings.get(76), user12, order20, false),
                        new Ticket(7, 6, screenings.get(76), user12, order20, false),
                        new Ticket(10, 11, screenings.get(76), user12, order20, true),
                        new Ticket(8, 11, screenings.get(76), user12, order20, false),
                        new Ticket(8, 5, screenings.get(76), user12, order20, false),
                        new Ticket(7, 17, screenings.get(76), user12, order20, true),
                        new Ticket(6, 10, screenings.get(76), user12, order20, true),
                        new Ticket(7, 8, screenings.get(76), user12, order20, false),
                        new Ticket(8, 17, screenings.get(76), user12, order20, true),
                        new Ticket(10, 10, screenings.get(76), user12, order20, false),
                        new Ticket(6, 1, screenings.get(76), user12, order20, false),
                        new Ticket(7, 13, screenings.get(76), user12, order20, true),
                        new Ticket(9, 13, screenings.get(76), user12, order20, false),
                        new Ticket(9, 19, screenings.get(76), user12, order20, false),
                        new Ticket(8, 19, screenings.get(76), user12, order20, true),
                        new Ticket(8, 4, screenings.get(76), user12, order20, true),
                        new Ticket(9, 12, screenings.get(76), user12, order20, false),
                        new Ticket(6, 20, screenings.get(76), user12, order20, true),
                        new Ticket(4, 23, screenings.get(76), user12, order20, false),
                        new Ticket(2, 16, screenings.get(76), user12, order20, true),
                        new Ticket(8, 21, screenings.get(76), user12, order20, false),
                        new Ticket(10, 12, screenings.get(76), user12, order20, true),
                        new Ticket(8, 12, screenings.get(76), user12, order20, true),
                        new Ticket(10, 4, screenings.get(76), user12, order20, true),
                        new Ticket(11, 10, screenings.get(76), user12, order20, false),
                        new Ticket(8, 15, screenings.get(76), user12, order20, true),
                        new Ticket(4, 18, screenings.get(76), user12, order20, false),
                        new Ticket(4, 10, screenings.get(76), user12, order20, true),
                        new Ticket(7, 17, screenings.get(76), user12, order20, false),
                        new Ticket(9, 15, screenings.get(76), user12, order20, false),
                        new Ticket(6, 16, screenings.get(76), user12, order20, false),
                        new Ticket(7, 21, screenings.get(76), user12, order20, false),
                        new Ticket(7, 12, screenings.get(76), user12, order20, false),
                        new Ticket(9, 14, screenings.get(76), user12, order20, false),
                        new Ticket(6, 10, screenings.get(76), user12, order20, false),
                        new Ticket(4, 11, screenings.get(76), user12, order20, true),
                        new Ticket(7, 17, screenings.get(76), user12, order20, true),
                        new Ticket(6, 7, screenings.get(76), user12, order20, false),
                        new Ticket(10, 16, screenings.get(76), user12, order20, false),
                        new Ticket(10, 16, screenings.get(76), user12, order20, true),
                        new Ticket(7, 17, screenings.get(76), user12, order20, true),
                        new Ticket(7, 21, screenings.get(76), user12, order20, true),
                        new Ticket(10, 17, screenings.get(76), user12, order20, true),
                        new Ticket(9, 18, screenings.get(76), user12, order20, true),
                        new Ticket(7, 11, screenings.get(76), user12, order20, true),
                        new Ticket(9, 13, screenings.get(76), user12, order20, false),
                        new Ticket(8, 14, screenings.get(76), user12, order20, false),
                        new Ticket(7, 16, screenings.get(76), user12, order20, true),
                        new Ticket(6, 25, screenings.get(76), user12, order20, true),
                        new Ticket(8, 7, screenings.get(76), user12, order20, false),
                        new Ticket(8, 19, screenings.get(76), user12, order20, false),
                        new Ticket(7, 15, screenings.get(76), user12, order20, false),
                        new Ticket(9, 14, screenings.get(76), user12, order20, true),
                        new Ticket(7, 14, screenings.get(76), user12, order20, true),
                        new Ticket(8, 12, screenings.get(76), user12, order20, true),
                        new Ticket(9, 9, screenings.get(76), user12, order20, false),
                        new Ticket(9, 11, screenings.get(76), user12, order20, false),
                        new Ticket(8, 16, screenings.get(76), user12, order20, false)
                );


                tickets2.forEach(ticket -> ticket.setUsed(true));
                tickets3.forEach(ticket -> ticket.setUsed(true));
                tickets4.forEach(ticket -> ticket.setUsed(true));
                tickets5.forEach(ticket -> ticket.setUsed(true));
                tickets7.forEach(ticket -> ticket.setUsed(true));
                tickets8.forEach(ticket -> ticket.setUsed(true));
                tickets9.forEach(ticket -> ticket.setUsed(true));
                tickets10.forEach(ticket -> ticket.setUsed(true));
                tickets11.forEach(ticket -> ticket.setUsed(true));
                tickets12.forEach(ticket -> ticket.setUsed(true));
                tickets13.forEach(ticket -> ticket.setUsed(true));
                tickets14.forEach(ticket -> ticket.setUsed(true));
                tickets15.forEach(ticket -> ticket.setUsed(true));
                tickets16.forEach(ticket -> ticket.setUsed(true));
                tickets17.forEach(ticket -> ticket.setUsed(true));
                tickets18.forEach(ticket -> ticket.setUsed(true));
                tickets19.forEach(ticket -> ticket.setUsed(true));

                calculateTotalPrice(order1, tickets1);
                calculateTotalPrice(order2, tickets2);
                calculateTotalPrice(order3, tickets3);
                calculateTotalPrice(order4, tickets4);
                calculateTotalPrice(order5, tickets5);
                calculateTotalPrice(order6, tickets6);
                calculateTotalPrice(order7, tickets7);
                calculateTotalPrice(order8, tickets8);
                calculateTotalPrice(order9, tickets9);
                calculateTotalPrice(order10, tickets10);
                calculateTotalPrice(order11, tickets11);
                calculateTotalPrice(order12, tickets12);
                calculateTotalPrice(order13, tickets13);
                calculateTotalPrice(order14, tickets14);
                calculateTotalPrice(order15, tickets15);
                calculateTotalPrice(order16, tickets16);
                calculateTotalPrice(order17, tickets17);
                calculateTotalPrice(order18, tickets18);
                calculateTotalPrice(order19, tickets19);
                calculateTotalPrice(order20, tickets20);

                orderRepository.saveAll(List.of(order1, order2, order3, order4, order5, order6, order7, order8, order9,
                        order10, order11, order12, order13, order14, order15, order16, order17, order18, order19, order20));

                ticketRepository.saveAll(tickets1);
                ticketRepository.saveAll(tickets2);
                ticketRepository.saveAll(tickets3);
                ticketRepository.saveAll(tickets4);
                ticketRepository.saveAll(tickets5);
                ticketRepository.saveAll(tickets6);
                ticketRepository.saveAll(tickets7);
                ticketRepository.saveAll(tickets8);
                ticketRepository.saveAll(tickets9);
                ticketRepository.saveAll(tickets10);
                ticketRepository.saveAll(tickets11);
                ticketRepository.saveAll(tickets12);
                ticketRepository.saveAll(tickets13);
                ticketRepository.saveAll(tickets14);
                ticketRepository.saveAll(tickets15);
                ticketRepository.saveAll(tickets16);
                ticketRepository.saveAll(tickets17);
                ticketRepository.saveAll(tickets18);
                ticketRepository.saveAll(tickets19);
                ticketRepository.saveAll(tickets20);

                List<Review> reviews = new ArrayList<>();

                reviews.add(new Review(BigDecimal.valueOf(4.0), "Świetny film, bardzo wciągający!", user3, movie1, LocalDateTime.of(2024, 11, 18, 14, 30)));
                reviews.add(new Review(BigDecimal.valueOf(3.5), "Dobry film, ale mogłoby być więcej akcji.", user4, movie2, LocalDateTime.of(2024, 11, 20, 10, 0)));
                reviews.add(new Review(BigDecimal.valueOf(5.0), "Absolutnie fenomenalna produkcja! Polecam każdemu!", user5, movie3, LocalDateTime.of(2024, 11, 22, 16, 45)));
                reviews.add(new Review(BigDecimal.valueOf(2.5), "Film przeciętny, nie spełnił moich oczekiwań.", user6, movie4, LocalDateTime.of(2024, 11, 24, 18, 30)));
                reviews.add(new Review(BigDecimal.valueOf(3.0), "Trochę nudnawy, ale ma swoje momenty.", user7, movie5, LocalDateTime.of(2024, 11, 26, 12, 15)));
                reviews.add(new Review(BigDecimal.valueOf(1.5), "Niestety, film bardzo słaby, nie polecam.", user8, movie6, LocalDateTime.of(2024, 11, 28, 20, 0)));
                reviews.add(new Review(BigDecimal.valueOf(4.5), "Fenomenalna historia, świetna gra aktorska.", user9, movie7, LocalDateTime.of(2024, 11, 30, 22, 0)));
                reviews.add(new Review(BigDecimal.valueOf(3.5), "Zdecydowanie lepszy niż się spodziewałem, ale coś było nie tak z tempem.", user10, movie8, LocalDateTime.of(2024, 12, 1, 19, 30)));
                reviews.add(new Review(BigDecimal.valueOf(4.0), "Zaskakująco dobry film, z wieloma emocjonalnymi momentami.", user11, movie9, LocalDateTime.of(2024, 12, 3, 17, 0)));
                reviews.add(new Review(BigDecimal.valueOf(3.0), "Dość przeciętny, momentami nudny.", user12, movie10, LocalDateTime.of(2024, 12, 5, 13, 30)));
                reviews.add(new Review(BigDecimal.valueOf(5.0), "Fantastyczne zakończenie! Cała seria wspaniała.", user3, movie11, LocalDateTime.of(2024, 12, 7, 14, 0)));
                reviews.add(new Review(BigDecimal.valueOf(2.0), "Słaby film, bardzo się zawiodłem.", user4, movie12, LocalDateTime.of(2024, 12, 9, 16, 45)));
                reviews.add(new Review(BigDecimal.valueOf(4.0), "Dobra produkcja, choć mogła być krótsza.", user5, movie13, LocalDateTime.of(2024, 12, 11, 10, 30)));
                reviews.add(new Review(BigDecimal.valueOf(3.5), "Fajny film, ale trochę za mało akcji, za dużo wątków.", user6, movie14, LocalDateTime.of(2024, 12, 13, 11, 0)));
                reviews.add(new Review(BigDecimal.valueOf(4.5), "Genialna produkcja! Wciągnęła mnie od pierwszej minuty.", user7, movie15, LocalDateTime.of(2024, 12, 15, 20, 0)));
                reviews.add(new Review(BigDecimal.valueOf(2.5), "Miejscami nudny, choć niektóre sceny były naprawdę dobre.", user8, movie16, LocalDateTime.of(2024, 12, 17, 22, 0)));
                reviews.add(new Review(BigDecimal.valueOf(1.0), "Film jest okropny, zupełnie nie warte uwagi.", user9, movie17, LocalDateTime.of(2024, 12, 19, 18, 15)));
                reviews.add(new Review(BigDecimal.valueOf(4.0), "Zaskakująco udana produkcja. Czuć klimat.", user10, movie18, LocalDateTime.of(2024, 12, 21, 14, 45)));
                reviews.add(new Review(BigDecimal.valueOf(3.0), "Mocno przeciętny, bez większego wow.", user11, movie19, LocalDateTime.of(2024, 12, 23, 12, 30)));
                reviews.add(new Review(BigDecimal.valueOf(4.5), "Cudowny film, pełen emocji. Na pewno go obejrzę jeszcze raz.", user12, movie20, LocalDateTime.of(2024, 12, 25, 19, 0)));
                reviews.add(new Review(BigDecimal.valueOf(3.0), "Film dobry, ale historia nieco przewidywalna.", user3, movie2, LocalDateTime.of(2024, 12, 27, 16, 0)));
                reviews.add(new Review(BigDecimal.valueOf(5.0), "Absolutnie niesamowity! Całkowicie zaskoczył mnie ten film.", user4, movie3, LocalDateTime.of(2024, 12, 29, 18, 30)));
                reviews.add(new Review(BigDecimal.valueOf(4.0), "Zdecydowanie dobry film, choć trochę za długi.", user5, movie4, LocalDateTime.of(2025, 1, 2, 11, 15)));
                reviews.add(new Review(BigDecimal.valueOf(3.5), "Zdecydowanie lepszy niż się spodziewałem, ale było kilka zbędnych scen.", user6, movie5, LocalDateTime.of(2025, 1, 4, 22, 0)));
                reviews.add(new Review(BigDecimal.valueOf(2.0), "Film bardzo słaby. Nuda przez większość czasu.", user7, movie6, LocalDateTime.of(2025, 1, 6, 13, 30)));
                reviews.add(new Review(BigDecimal.valueOf(4.0), "Wciągająca fabuła, dobra gra aktorska, ale widać małe niedociągnięcia.", user8, movie7, LocalDateTime.of(2025, 1, 8, 15, 0)));
                reviews.add(new Review(BigDecimal.valueOf(5.0), "Perfekcyjna produkcja. Jeden z najlepszych filmów, jakie widziałem.", user9, movie8, LocalDateTime.of(2025, 1, 10, 17, 45)));
                reviews.add(new Review(BigDecimal.valueOf(3.0), "Film ma swoje momenty, ale jest trochę za długi.", user10, movie9, LocalDateTime.of(2025, 1, 12, 10, 0)));
                reviews.add(new Review(BigDecimal.valueOf(1.5), "Totalna klapa. Nieznośnie nudny film.", user11, movie10, LocalDateTime.of(2025, 1, 14, 19, 30)));
                reviews.add(new Review(BigDecimal.valueOf(4.5), "Świetny film, choć zakończenie mogłoby być lepsze.", user12, movie11, LocalDateTime.of(2025, 1, 16, 22, 0)));
                reviews.add(new Review(BigDecimal.valueOf(3.0), "Nie jest to film, który będę pamiętał na długo.", user3, movie12, LocalDateTime.of(2025, 1, 18, 12, 30)));
                reviews.add(new Review(BigDecimal.valueOf(2.5), "Film średni, ale nie najgorszy.", user4, movie13, LocalDateTime.of(2025, 1, 20, 14, 0)));
                reviews.add(new Review(BigDecimal.valueOf(5.0), "Absolutna perfekcja. Historia wciągnęła mnie od pierwszych minut.", user5, movie14, LocalDateTime.of(2025, 1, 22, 16, 15)));
                reviews.add(new Review(BigDecimal.valueOf(4.0), "Dobre tempo, świetne efekty specjalne, chociaż fabuła momentami zbyt przewidywalna.", user6, movie15, LocalDateTime.of(2025, 1, 23, 19, 0)));
                reviews.add(new Review(BigDecimal.valueOf(3.5), "Całkiem niezły film, ale mam zastrzeżenia co do zakończenia.", user7, movie16, LocalDateTime.of(2024, 11, 19, 20, 0)));
                reviews.add(new Review(BigDecimal.valueOf(4.5), "Cudowny film. Niezapomniane wrażenia!", user8, movie17, LocalDateTime.of(2024, 12, 3, 17, 0)));
                reviews.add(new Review(BigDecimal.valueOf(2.5), "Przeciętny film, którego nie będę pamiętał długo.", user9, movie18, LocalDateTime.of(2024, 12, 7, 18, 30)));
                reviews.add(new Review(BigDecimal.valueOf(5.0), "Niezwykła produkcja. Zdecydowanie najlepszy film tego roku.", user10, movie19, LocalDateTime.of(2024, 12, 11, 21, 0)));
                reviews.add(new Review(BigDecimal.valueOf(3.0), "Film ok, ale niczego nie wnosi nowego.", user11, movie20, LocalDateTime.of(2024, 12, 15, 19, 30)));
                reviews.add(new Review(BigDecimal.valueOf(4.5), "Genialny film, pełen emocji.", user12, movie1, LocalDateTime.of(2024, 12, 18, 14, 0)));
                reviews.add(new Review(BigDecimal.valueOf(4.0), "Zdecydowanie dobry film, choć trochę za długi.", user3, movie2, LocalDateTime.of(2025, 1, 3, 17, 0)));
                reviews.add(new Review(BigDecimal.valueOf(3.5), "Dobre tempo, ale końcówka była przewidywalna.", user4, movie3, LocalDateTime.of(2025, 1, 5, 11, 0)));



                reviewRepository.saveAll(reviews);
            }
        };
    }
}
