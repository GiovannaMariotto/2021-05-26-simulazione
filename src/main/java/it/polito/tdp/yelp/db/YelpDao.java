package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.yelp.model.Arco;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {

	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getAllCitta(){
		String sql = "SELECT DISTINCT (b.city) "
				+ "FROM business b, reviews r "
				+ "WHERE b.business_id=r.business_id "
				+ "ORDER BY b.city ASC  ";
				
		List<String> result = new LinkedList<>();
		
	Connection conn = DBConnect.getConnection();
	try {
		PreparedStatement st = conn.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			result.add(rs.getString("city"));
			}
		rs.close();
		st.close();
		conn.close();
		return result;
		
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}
	
	}
	
	public List<Business> getBusinessByCityAndYear(String citta, Year anno){
		String sql="SELECT * "
				+ "FROM business "
				+ "WHERE city=? AND ( "
								+ "SELECT COUNT(*) "
								+ "FROM reviews "
								+ "WHERE business.business_id=reviews.business_id AND YEAR(reviews.review_date)=? "
								+ ") > 0 "
				+ "ORDER BY business.business_name ASC ";
	Connection conn = DBConnect.getConnection();
	List<Business> result=new LinkedList<>();
	try{
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, citta);
		st.setInt(2, anno.getValue());
		ResultSet res = st.executeQuery();
		while(res.next()) {
			Business business = new Business(res.getString("business_id"), 
					res.getString("full_address"),
					res.getString("active"),
					res.getString("categories"),
					res.getString("city"),
					res.getInt("review_count"),
					res.getString("business_name"),
					res.getString("neighborhoods"),
					res.getDouble("latitude"),
					res.getDouble("longitude"),
					res.getString("state"),
					res.getDouble("stars"));
			result.add(business);
		}
		
	}catch(SQLException e) {
		throw new RuntimeException("ERROR: DB getLocali",e);
	}
	return result;
		
		
		
	}
	
	public void getMedia(Map<String,Business> idMap, String citta) throws SQLException {
		String sql = "SELECT b1.business_id AS id1, b2.business_id AS id2, AVG(r1.stars) -AVG(r2.stars) AS peso "
				+ "FROM business b1, business b2,reviews r1, reviews r2 "
				+ "WHERE b1.business_id=r1.business_id AND r2.business_id=b2.business_id "
				+ "AND b1.city=b2.city AND b1.city= 'Phoenix' "
				+ "AND YEAR(r1.review_date) = YEAR(r2.review_date) AND YEAR(r1.review_date)=2005 "
				+ "AND b1.business_id <> b2.business_id "
				+ "GROUP BY b1.business_id, b2.business_id "
				+ "HAVING peso>0 ";
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,citta);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Business b = idMap.get(rs.getString("business_id"));
				b.setMedia(rs.getDouble("media"));
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return;
		}
		
		
		
	}
		
	public List<Arco> getArchi(String citta, Year anno){
		String sql ="SELECT b1.business_id AS id1, b2.business_id AS id2, AVG(r1.stars) -AVG(r2.stars) AS peso "
				+ "FROM business b1, business b2,reviews r1, reviews r2 "
				+ "WHERE b1.business_id=r1.business_id AND r2.business_id=b2.business_id "
				+ "AND b1.city=b2.city AND b1.city=? "
				+ "AND YEAR(r1.review_date) = YEAR(r2.review_date) AND YEAR(r1.review_date)=? "
				+ "AND b1.business_id <> b2.business_id "
				+ "GROUP BY b1.business_id, b2.business_id "
				+ "HAVING peso>0";
		List<Arco> archi = new ArrayList();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,citta);
			st.setInt(2, anno.getValue());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				
				archi.add(new Arco(rs.getString("id1"),rs.getString("id2"), rs.getDouble("peso")));
			}	
				
			}catch(SQLException e) {
				throw new RuntimeException("ERRORE: DB getArchi",e);
			}
		return archi;
		
		
		
		
	}
	
	
	
	
}
