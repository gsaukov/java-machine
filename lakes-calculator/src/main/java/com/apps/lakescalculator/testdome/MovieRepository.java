package com.apps.lakescalculator.testdome;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.*;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.jdbc.core.*;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.stereotype.Repository;
//import javax.annotation.PostConstruct;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.*;
//
//class Movie {
//    public String name;
//    public int year;
//    public int rating;
//
//    public Movie(String name, int year, int rating) {
//        this.name = name;
//        this.year = year;
//        this.rating = rating;
//    }
//}
//@Configuration
//@Import(MovieRepository.class)
//class Config {
//    @Bean
//    public DriverManagerDataSource dataSource() {
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName("org.h2.Driver");
//        ds.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
//        return ds;
//    }
//    @Bean
//    public JdbcTemplate jdbcTemplate(DriverManagerDataSource ds) {
//        return new JdbcTemplate(ds);
//    }
//}
//
//@Repository
public class MovieRepository {
//
//    @Autowired
//    private JdbcTemplate template;
//
//    @PostConstruct
//    public void createTable() {
//        template.execute("CREATE TABLE movies (id bigint auto_increment primary key, name VARCHAR(50), year int, rating int)");
//    }
//
//    public void createMovie(String name, int year, int rating) {
//        template.update("INSERT INTO movies(name, year, rating) VALUES(?,?,?)", name, year, rating);
//    }
//
//    public List<Movie> findMoviesByName (String name) {
//
//        String sql = "SELECT * FROM movies where name like :name";
//
//        List<Movie> movies = new ArrayList<>();
//
//
//        Map<String,Object> params = new HashMap<String,Object>();
//        params.put("name", name);
//
//        return template.query(sql, new MovieRowMaper(), params);
//    }
//
//    public class MovieRowMaper implements RowMapper<Movie> {
//        public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
//            String name = (String) rs.getString("name");
//            int year = rs.getInt("year");
//            int rating = rs.getInt("rating");
//            return new Movie(name, year, rating);
//        }
//    }
//
//    public static void main(String[] args) {
//        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
//        config.register(Config.class);
//        config.refresh();
//        MovieRepository repository = config.getBean(MovieRepository.class);
//
//        repository.createMovie("Some movie", 1974, 3);
//        repository.createMovie("Some other movie", 1993, 2);
//
//        List<Movie> movies = repository.findMoviesByName("Some%");
//        for(Movie movie : movies) {
//            System.out.println(movie.name + " - " + movie.year + " - " + movie.rating);
//        }
//    }
}