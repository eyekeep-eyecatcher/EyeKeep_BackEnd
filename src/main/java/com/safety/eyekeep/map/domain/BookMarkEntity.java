package com.safety.eyekeep.map.domain;

import com.safety.eyekeep.map.dto.BookMarkDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Book_Mark")
public class BookMarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column
    @ElementCollection
    @CollectionTable(name = "Book_Mark_List", joinColumns = @JoinColumn(name = "Book_Mark_id"))
    private List<BookMark> list;

    public BookMarkDTO toDTO() {
        BookMarkDTO bookMarkDTO = new BookMarkDTO();
        bookMarkDTO.setUsername(username);
        bookMarkDTO.setList(list);
        return bookMarkDTO;
    }
}


