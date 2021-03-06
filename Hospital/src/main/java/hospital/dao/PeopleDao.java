package hospital.dao;

import hospital.types.People;

import java.util.List;

/**
 * Polytech
 * Created by igor on 07.04.17.
 */
public interface PeopleDao {
    List<People> findAll();

    People insert(People people);

    void deleteById(long id);

    void deleteByWardId(long id);
}
