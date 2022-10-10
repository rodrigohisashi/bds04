package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.exceptions.DataBaseException;
import com.devsuperior.bds04.exceptions.ResourceNotFoundException;
import com.devsuperior.bds04.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    @Autowired
    private CityRepository repository;

    @Transactional(readOnly = true)
    public List<CityDTO> findAllPaged() {
        List<City> list = repository.findByOrderByNameAsc();

        return list.stream().map(CityDTO::new).toList();

    }

    @Transactional(readOnly = true)
    public CityDTO findById(long id) {
        Optional<City> obj = repository.findById(id);
        City entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada!"));
        return new CityDTO(entity);
    }

    @Transactional
    public CityDTO insert(CityDTO dto) {
        City entity = new City();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CityDTO(entity);
    }

    @Transactional
    public CityDTO update(Long id, CityDTO dto) {
        try {
            City entity = repository.getOne(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CityDTO(entity);
        } catch(EntityNotFoundException e) {
            throw new ResourceNotFoundException("O id " + id + " não foi encontrado." );
        }

    }

    public void delete(long id) {
        try {
            repository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("O id " + id + " não foi encontrado." );

        } catch(DataIntegrityViolationException e) {
            throw new DataBaseException("Violação de integridade no banco de dados");
        }
    }


}
