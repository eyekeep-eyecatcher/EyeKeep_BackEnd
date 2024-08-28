package com.safety.eyekeep.map.domain;

import com.safety.eyekeep.map.dto.CCTVDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Seoul_CCTV")
public class CCTVEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String address;

    @Column
    private String latitude;

    @Column
    private String longitude;

    public CCTVDTO toDTO(){
        CCTVDTO cctvDTO = new CCTVDTO();
        cctvDTO.setAddress(null);
        cctvDTO.setLatitude(this.latitude);
        cctvDTO.setLongitude(this.longitude);
        return cctvDTO;
    }

}
