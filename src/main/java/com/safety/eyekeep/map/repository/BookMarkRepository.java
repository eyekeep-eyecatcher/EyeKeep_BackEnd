package com.safety.eyekeep.map.repository;

import com.safety.eyekeep.map.domain.BookMarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMarkEntity, Long> {
    @Query(value = "SELECT * FROM Book_Mark BM JOIN Book_Mark_List BML ON BM.id = BML.Book_Mark_id WHERE BML.location_name = :locationName AND BM.username = :username", nativeQuery = true)
    BookMarkEntity findByUsernameAndLocationName(@Param("locationName") String locationName, @Param("username") String username);

    BookMarkEntity findByUsername(String username);
}
