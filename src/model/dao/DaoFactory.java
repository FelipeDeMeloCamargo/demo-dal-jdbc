package model.dao;

import model.dao.impl.SellerDaoJDBC;

//responsavel para instanciar os Daos
public class DaoFactory {

	public static SellerDao createSellerDao(){ //retorna o tipo da interface com a variavel CreateSeller
		return new SellerDaoJDBC(); 
	}
}
