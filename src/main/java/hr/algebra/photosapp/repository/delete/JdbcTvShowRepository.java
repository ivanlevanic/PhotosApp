//package hr.algebra.photosapp.repository.delete;
//
//import hr.algebra.photosapp.domain.delete.TvShowData;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.stereotype.Repository;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public class JdbcTvShowRepository implements TvShowRepository {
//
//    private static final String TABLE_NAME = "tv_show_data";
//    private static final String GENERATED_KEY_COLUMN = "id";
//
//    private final JdbcTemplate jdbc;
//    private final SimpleJdbcInsert jdbcInsert;
//
//    public JdbcTvShowRepository(JdbcTemplate jdbc) {
//        this.jdbc = jdbc;
//        this.jdbcInsert = new SimpleJdbcInsert(jdbc)
//                .withTableName(TABLE_NAME)
//                .usingGeneratedKeyColumns(GENERATED_KEY_COLUMN);
//    }
//
//    @Override
//    public List<Optional<TvShowData>> getCheapestStreamingService(String name) {
//        String request = "SELECT * FROM TV_SHOW_DATA WHERE name LIKE"
//                       + " '%" + name + "%' "
//                       + "AND price = ( SELECT min(price) FROM TV_SHOW_DATA WHERE name LIKE"  + " '%" + name + "%' " + ")";
//        try{
//            List<Optional<TvShowData>> results = jdbc.query(request, (rs, arg1) -> Optional.ofNullable(this.mapRowToShow(rs, arg1)));
//            return results;
//        } catch (EmptyResultDataAccessException e){
//            return List.of();
//        }
//    }
//
//    private TvShowData mapRowToShow(ResultSet rs, int rowNum) throws SQLException {
//        return new TvShowData(
//                rs.getLong("id"),
//                rs.getString("name"),
//                rs.getFloat("price"),
//                rs.getString("streamingservice"),
//                rs.getInt("seasons")
//                //rs.getString("fullname")
//        );
//    }
//
//}
