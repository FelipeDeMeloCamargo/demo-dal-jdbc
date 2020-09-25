package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	private Connection conn;
	
	public SellerDaoJDBC (Connection conn) {   //construtor para realizar a conexao
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) { //selects para buscar dados no banco
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.* ,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id=?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			//transformar os dados do SQL para orientado a objeto para gravar na memoria
			if (rs.next()) { //retorna se a consulta retornou alguma informação
				Department dep = instantiateDepartment(rs);//pegamos o nome do departamento
				Seller obj = instantiateSeller(rs,dep); //é DEP pois instanciamos um Department e passará todos os dados informados
				return obj;	
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.CloseResultSet(rs); //fechando os recursos
			DB.CloseStatement(st);
		}
		
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException { //excessao propagada
		Seller obj = new Seller();
		obj.setID(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthdate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException { //nova função
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));//pegamos o id
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() { //buscar todos vendedores
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.* ,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name ");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();//map para realizar a verificação de duplicidade, não aceita repeticoes
			
			//transformar os dados do SQL para orientado a objeto para gravar na memoria
			while (rs.next()) {  //While serve para validar e não retornar a duplicidade
				
				Department dep = map.get(rs.getInt("DepartmentId")); //dep recebe o id do Departamento
				
				if(dep == null) { //valida se o valor já existe ou não
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep); //salvar o departamento para o dep
					 
				}
			
				Seller obj = instantiateSeller(rs,dep); //é DEP pois instanciamos um Department e passará todos os dados informados
				list.add(obj); //faz tudo e adiciona na lista
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.CloseResultSet(rs); //fechando os recursos
			DB.CloseStatement(st);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.* ,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name ");
			
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();//map para realizar a verificação de duplicidade, não aceita repeticoes
			
			//transformar os dados do SQL para orientado a objeto para gravar na memoria
			while (rs.next()) {  //While serve para validar e não retornar a duplicidade
				
				Department dep = map.get(rs.getInt("DepartmentId")); //dep recebe o id do Departamento
				
				if(dep == null) { //valida se o valor já existe ou não
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep); //salvar o departamento para o dep
					 
				}
			
				Seller obj = instantiateSeller(rs,dep); //é DEP pois instanciamos um Department e passará todos os dados informados
				list.add(obj); //faz tudo e adiciona na lista
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.CloseResultSet(rs); //fechando os recursos
			DB.CloseStatement(st);
		}
		
	}

}
