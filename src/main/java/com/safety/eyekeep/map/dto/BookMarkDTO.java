package com.safety.eyekeep.map.dto;

import com.safety.eyekeep.map.domain.BookMarkEntity;
import com.safety.eyekeep.map.domain.BookMark;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookMarkDTO {
    private String username;
    private List<BookMark> list;

    public BookMarkEntity toEntity(){
        BookMarkEntity bookMarkEntity = new BookMarkEntity();
        bookMarkEntity.setUsername(this.username);
        bookMarkEntity.setList(this.list);
        return bookMarkEntity;
    }
}
