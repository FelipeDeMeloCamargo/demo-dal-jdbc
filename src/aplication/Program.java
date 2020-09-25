package aplication;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerdao = DaoFactory.createSellerDao();// instanciar um sellerDao que recebe o que vem da
															// DaoFactory, independentemente do que seja.

		System.out.println("=====Teste 1: Seller findById =====");
		Seller seller = sellerdao.findById(3);
		System.out.println(seller);

		System.out.println("\n=====Teste 2: Seller findByDepartment =====");
		Department department = new Department(2, null);// passado como função e é gravado em department
		List<Seller> list = sellerdao.findByDepartment(department);// em seguida department é adicionado na lista

		for (Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println("\n=====Teste 3: Seller findAll =====");
		list = sellerdao.findAll();// em seguida department é adicionado na lista

		for (Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println("\n=====Teste 4: Seller Insert =====");
		Seller newSeller = new Seller(null,"Greg","greg@gmail.com", new Date(), 4000.0,department);
		//inserir no banco de dados
		sellerdao.insert(newSeller);
		System.out.println("Inserted! New id = " + newSeller.getID());//retornará o id do new seller

	}

}
