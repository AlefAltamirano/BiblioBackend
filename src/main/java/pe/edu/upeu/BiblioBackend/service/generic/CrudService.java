package pe.edu.upeu.BiblioBackend.service.generic;

import java.util.List;

public interface CrudService<REQ, RES, ID> {
    RES create(REQ request);
    RES update(ID id, REQ request);
    RES read(ID id);
    void delete(ID id);
    List<RES> readAll();
}