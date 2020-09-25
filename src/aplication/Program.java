package aplication;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		
		SellerDao sellerdao = DaoFactory.createSellerDao();//instanciar um sellerDao que recebe o que vem da DaoFactory, independentemente do que seja.
		
		Seller seller = sellerdao.findById(3);
		
		System.out.println(seller);
	}

}
