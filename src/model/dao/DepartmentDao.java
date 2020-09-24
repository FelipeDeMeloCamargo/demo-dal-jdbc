package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	//Interface
	
	void insert(Department obj); //insert no banco de dados
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id);
	
	List<Department> findAll();//retorna todos

}
