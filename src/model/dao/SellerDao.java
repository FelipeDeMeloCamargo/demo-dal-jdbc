package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	
	//Interface
	
	void insert(Seller obj); //insert no banco de dados
	void update(Seller obj);
	void deleteById(Integer id);
	Seller findById(Integer id);
	
	List<Seller> findAll();//retorna todos
	List<Seller> findByDepartment(Department department);//retorna por departamento

}