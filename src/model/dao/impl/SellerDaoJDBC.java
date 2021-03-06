package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);//retorna o ID do vendedor inserido
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthdate().getTime()));//data que vem do banco sql
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId()); 
			
			int rowsAffected = st.executeUpdate(); //realiza o update
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();//retorna os ids
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setID(id);
				}
				DB.CloseResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! no rows affected!"); //excessao caso n�o retorne nenhuma linha de id. Defensiva
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.CloseStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " 
					+ "WHERE Id = ?"); 
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthdate().getTime()));//data que vem do banco sql
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId()); 
			st.setInt(6, obj.getID());
			
			st.executeUpdate(); //realiza o update
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.CloseStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			
			st.setInt(1, id);
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		}
		finally {
			DB.CloseStatement(st);
		}
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
			if (rs.next()) { //retorna se a consulta retornou alguma informa��o
				Department dep = instantiateDepartment(rs);//pegamos o nome do departamento
				Seller obj = instantiateSeller(rs,dep); //� DEP pois instanciamos um Department e passar� todos os dados informados
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

	private Department instantiateDepartment(ResultSet rs) throws SQLException { //nova fun��o
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
			Map<Integer, Department> map = new HashMap<>();//map para realizar a verifica��o de duplicidade, n�o aceita repeticoes
			
			//transformar os dados do SQL para orientado a objeto para gravar na memoria
			while (rs.next()) {  //While serve para validar e n�o retornar a duplicidade
				
				Department dep = map.get(rs.getInt("DepartmentId")); //dep recebe o id do Departamento
				
				if(dep == null) { //valida se o valor j� existe ou n�o
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep); //salvar o departamento para o dep
					 
				}
			
				Seller obj = instantiateSeller(rs,dep); //� DEP pois instanciamos um Department e passar� todos os dados informados
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
			Map<Integer, Department> map = new HashMap<>();//map para realizar a verifica��o de duplicidade, n�o aceita repeticoes
			
			//transformar os dados do SQL para orientado a objeto para gravar na memoria
			while (rs.next()) {  //While serve para validar e n�o retornar a duplicidade
				
				Department dep = map.get(rs.getInt("DepartmentId")); //dep recebe o id do Departamento
				
				if(dep == null) { //valida se o valor j� existe ou n�o
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep); //salvar o departamento para o dep
					 
				}
			
				Seller obj = instantiateSeller(rs,dep); //� DEP pois instanciamos um Department e passar� todos os dados informados
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
