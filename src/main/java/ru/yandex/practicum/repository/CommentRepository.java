package ru.yandex.practicum.repository;


import org.springframework.data.repository.CrudRepository;
import ru.yandex.practicum.DAO.CommentDAO;

public interface CommentRepository extends CrudRepository<CommentDAO, Long> {

}
