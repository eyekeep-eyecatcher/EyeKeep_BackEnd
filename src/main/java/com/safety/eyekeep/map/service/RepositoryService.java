package com.safety.eyekeep.map.service;

import com.safety.eyekeep.map.domain.*;
import com.safety.eyekeep.map.dto.*;
import com.safety.eyekeep.map.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RepositoryService {
    private final PoliceOfficeRepository policeOfficeRepository;
    private final SecurityLightRepository securityLightRepository;
    private final CCTVRepository cctvRepository;
    private final ChildrenProtectionRepository childrenProtectionRepository;
    private final RoadNetworkNodeRepository roadNetworkNodeRepository;
    private final RoadNetworkLinkRepository roadNetworkLinkRepository;
    private final ChildrenGuardHouseRepository childrenGuardHouseRepository;
    private final SafetyEmergencyBellRepository safetyEmergencyBellRepository;
    private final AccidentBlackSpotRepository accidentBlackSpotRepository;
    private final BookMarkRepository bookMarkRepository;

    public void saveAllPoliceOfficeRepository(List<PoliceOfficeDTO> policeOfficeDTOs) {
        List<PoliceOfficeEntity> policeOfficeEntities = policeOfficeDTOs.stream()
                .map(PoliceOfficeDTO::toEntity)
                .toList();
        policeOfficeRepository.saveAll(policeOfficeEntities);
    }

    public void saveAllSecurityLightRepository(List<SecurityLightDTO> securityLightDTOs) {
        List<SecurityLightEntity> securityLightEntities = securityLightDTOs.stream()
                .map(SecurityLightDTO::toEntity)
                .toList();
        securityLightRepository.saveAll(securityLightEntities);
    }

    public void saveAllCCTVRepository(List<CCTVDTO> cctvDTOs) {
        List<CCTVEntity> cctvEntities = cctvDTOs.stream()
                .map(CCTVDTO::toEntity)
                .toList();
        cctvRepository.saveAll(cctvEntities);
    }

    public void saveAllChildrenProtectRepository(List<ChildrenProtectionDTO> childrenProtectionDTOs) {
        List<ChildrenProtectionEntity> childrenProtectEntities = childrenProtectionDTOs.stream()
                .map(ChildrenProtectionDTO::toEntity)
                .toList();
        childrenProtectionRepository.saveAll(childrenProtectEntities);
    }

    public void saveAllRoadNetworkNodeRepository(List<RoadNetworkNodeDTO> roadNetworkNodeDTOs) {
        List<RoadNetworkNodeEntity> loadNetworkNodeEntities = roadNetworkNodeDTOs.stream()
                .map(RoadNetworkNodeDTO::toEntity)
                .toList();
        roadNetworkNodeRepository.saveAll(loadNetworkNodeEntities);
    }

    public void saveAllRoadNetworkLinkRepository(List<RoadNetworkLinkDTO> roadNetworkLinkDTOs) {
        List<RoadNetworkLinkEntity> loadNetworkLinkEntities = roadNetworkLinkDTOs.stream()
                .map(RoadNetworkLinkDTO::toEntity)
                .toList();
        roadNetworkLinkRepository.saveAll(loadNetworkLinkEntities);
    }

    public void saveAllChildrenGuardHouseRepository(List<ChildrenGuardHouseDTO> childrenGuardHouseDTOs) {
        List<ChildrenGuardHouseEntity> childrenGuardHouseEntities = childrenGuardHouseDTOs.stream()
                .map(ChildrenGuardHouseDTO::toEntity)
                .toList();
        childrenGuardHouseRepository.saveAll(childrenGuardHouseEntities);
    }

    public void saveAllSafetyEmergencyBellRepository(List<SafetyEmergencyBellDTO> safetyEmergencyBellDTOs) {
        List<SafetyEmergencyBellEntity> safetyEmergencyBellEntities = safetyEmergencyBellDTOs.stream()
                .map(SafetyEmergencyBellDTO::toEntity)
                .toList();
        safetyEmergencyBellRepository.saveAll(safetyEmergencyBellEntities);
    }

    public void saveAllAccidentBlackSpotRepository(List<AccidentBlackSpotDTO> accidentBlackSpotDTOs) {
        List<AccidentBlackSpotEntity> accidentBlackSpotEntities = accidentBlackSpotDTOs.stream()
                .map(AccidentBlackSpotDTO::toEntity)
                .toList();
        accidentBlackSpotRepository.saveAll(accidentBlackSpotEntities);
    }

    public void saveAllRoadNetworkLinkRepositoryToEntity(List<RoadNetworkLinkEntity> roadNetworkLinkEntities) {
        roadNetworkLinkRepository.saveAll(roadNetworkLinkEntities);
    }

    public List<PoliceOfficeDTO> findAllPoliceOfficeRepository() {
        return policeOfficeRepository.findAll().stream()
                .map(PoliceOfficeEntity::toDTO)
                .toList();
    }

    public List<SecurityLightDTO> findAllSecurityLightRepository() {
        return securityLightRepository.findAll().stream()
                .map(SecurityLightEntity::toDTO)
                .toList();
    }

    public List<CCTVDTO> findAllCCTVRepository() {
        return cctvRepository.findAll().stream()
                .map(CCTVEntity::toDTO)
                .toList();
    }

    public List<ChildrenProtectionDTO> findAllChildrenProtectRepository() {
        return childrenProtectionRepository.findAll().stream()
                .map(ChildrenProtectionEntity::toDTO)
                .toList();
    }

    public List<ChildrenGuardHouseDTO> findAllChildrenGuardHouseRepository() {
        return childrenGuardHouseRepository.findAll().stream()
                .map(ChildrenGuardHouseEntity::toDTO)
                .toList();
    }

    public List<SafetyEmergencyBellDTO> findAllSafetyEmergencyBellRepository() {
        return safetyEmergencyBellRepository.findAll().stream()
                .map(SafetyEmergencyBellEntity::toDTO)
                .toList();
    }

    public List<AccidentBlackSpotDTO> findAllAccidentBlackSpotRepository() {
        return accidentBlackSpotRepository.findAll().stream()
                .map(AccidentBlackSpotEntity::toDTO)
                .toList();
    }

    public List<RoadNetworkLinkEntity> findAllRoadNetworkLinkRepositoryToEntity() {
        List<RoadNetworkLinkEntity> roadNetworkLinkEntities = roadNetworkLinkRepository.findAll();
        for (RoadNetworkLinkEntity roadNetworkLinkEntity : roadNetworkLinkEntities) {
            Hibernate.initialize(roadNetworkLinkEntity.getLink());
        }
        return roadNetworkLinkEntities;
    }

    public Double countPoliceOfficeRepositoryWithinEllipse(double latitude1, double longitude1, double latitude2, double longitude2, double majorAxisLength) {
        return policeOfficeRepository.countPoliceOfficesWithinEllipse(latitude1, longitude1, latitude2, longitude2, majorAxisLength);
    }

    public List<RoadNetworkLinkDTO> findWithInRadiusAtRoadNetworkLinkRepository(double centerLatitude, double centerLongitude, double radius) {
        List<RoadNetworkLinkEntity> roadNetworkLinkEntities = roadNetworkLinkRepository.findLinksWithInRadius(centerLatitude, centerLongitude, radius);
        // 엔티티의 컬렉션을 초기화
        for (RoadNetworkLinkEntity entity : roadNetworkLinkEntities) {
            Hibernate.initialize(entity.getLink().size());
        }
        return roadNetworkLinkEntities.stream()
                .map(RoadNetworkLinkEntity::toDTO)
                .toList();
    }

    public List<RoadNetworkNodeDTO> findWithInRadiusAtRoadNetworkNodeRepository(double centerLatitude, double centerLongitude, double radius) {
        List<RoadNetworkNodeEntity> roadNetworkNodeEntities = roadNetworkNodeRepository.findNodesWithInRadius(centerLatitude, centerLongitude, radius);
        return roadNetworkNodeEntities.stream()
                .map(RoadNetworkNodeEntity::toDTO)
                .toList();
    }

    public boolean existsBookMarkByUsernameAndLocationName(BookMarkDTO bookMarkDTO) {
        String username = bookMarkDTO.getUsername();
        String locationName = bookMarkDTO.getList().get(0).getLocationName();
        BookMarkEntity bookMarkEntity = bookMarkRepository.findByUsernameAndLocationName(locationName, username);
        return bookMarkEntity != null;
    }

    public BookMark saveBookMarkRepository(BookMarkDTO bookMarkDTO) {
        BookMarkEntity oldEntity = bookMarkRepository.findByUsername(bookMarkDTO.getUsername());
        if (oldEntity == null) {
            // 기존 사용자의 엔티티가 없을 경우 새로 생성해 저장.
            BookMarkEntity bookMarkEntity = bookMarkDTO.toEntity();
            BookMarkEntity newEntity = bookMarkRepository.save(bookMarkEntity);
            return newEntity.getList().get(0);

        }
        else {
            // 기존 사용자의 엔티티가 있을 경우 기존 List에 추가하는 형식.
            List<BookMark> bookMarks = oldEntity.getList();
            BookMark bookMark = bookMarkDTO.getList().get(0);
            bookMarks.add(bookMark);
            oldEntity.setList(bookMarks);
            BookMarkEntity savedEntity = bookMarkRepository.save(oldEntity);
            List<BookMark> bookMarkList = savedEntity.getList();
            for (BookMark savedBookMark : bookMarkList) {
                if(savedBookMark.getLocationName().equals(bookMark.getLocationName())) {
                    return savedBookMark;
                }
            }
            return null;
        }
    }

    public BookMark setAliasBookMarkRepository(BookMarkDTO bookMarkDTO) {
        String username = bookMarkDTO.getUsername();
        BookMark bookMark = bookMarkDTO.getList().get(0);
        String alias = bookMark.getAlias();
        String locationName = bookMark.getLocationName();
        BookMarkEntity bookMarkEntity = bookMarkRepository.findByUsername(username);
        if (bookMarkEntity == null) {
            // 해당 북마크가 존재하지 않을 경우
            return null;
        }

        // 해당 북마크가 존재할 경우 별명 수정 or 설정
        List<BookMark> bookMarks = bookMarkEntity.getList();
        boolean flag = false;
        for (BookMark mark : bookMarks) {
            if (mark.getLocationName().equals(locationName)) {
                mark.setAlias(alias);
                flag = true;
            }
        }
        if (!flag) {
            return null;
        }
        bookMarkEntity.setList(bookMarks);
        BookMarkEntity savedEntity = bookMarkRepository.save(bookMarkEntity);
        List<BookMark> bookMarkList = savedEntity.getList();
        for (BookMark savedBookMark : bookMarkList) {
            if(savedBookMark.getLocationName().equals(bookMark.getLocationName())) {
                return savedBookMark;
            }
        }
        return null;
    }

    public BookMarkDTO findBookMarkEntityByUsername(String username) {
        BookMarkEntity bookMarkEntity = bookMarkRepository.findByUsername(username);
        if(bookMarkEntity != null) {
            // LazyInitializationException 에러 발생 방지를 위함.
            Hibernate.initialize(bookMarkEntity.getList());
            return bookMarkEntity.toDTO();
        }
        return null;
    }

    public HttpStatus deleteBookMarkByUsernameAndLocationName(BookMarkDTO bookMarkDTO) {
        String username = bookMarkDTO.getUsername();
        BookMark bookMark = bookMarkDTO.getList().get(0);
        String locationName = bookMark.getLocationName();
        BookMarkEntity bookMarkEntity = bookMarkRepository.findByUsername(username);
        if (bookMarkEntity == null) {
            return HttpStatus.NOT_FOUND;
        }

        List<BookMark> bookMarks = bookMarkEntity.getList();
        boolean flag = false;
        Iterator<BookMark> iterator = bookMarks.iterator();

        while (iterator.hasNext()) {
            BookMark mark = iterator.next();
            if (mark.getLocationName().equals(locationName)) {
                iterator.remove();
                flag = true;
            }
        }

        if (!flag) {
            return HttpStatus.BAD_REQUEST;
        }
        bookMarkEntity.setList(bookMarks);
        bookMarkRepository.save(bookMarkEntity);
        return HttpStatus.OK;
    }
}
