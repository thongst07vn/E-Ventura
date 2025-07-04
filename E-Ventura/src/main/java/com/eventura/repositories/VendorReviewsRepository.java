package com.eventura.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



import com.eventura.entities.VendorReviews;

@Repository
public interface VendorReviewsRepository extends JpaRepository<VendorReviews, Integer> {
	@Query("FROM VendorReviews WHERE vendors.id = :id")
	public List<VendorReviews>  findVendorReview(@Param("id") int id);
	
	@Query("select count(v) from VendorReviews v where v.vendors.id = :id")
	public int countVendorReview(@Param("id") int id);
	
	@Query("select avg(v.rating) from VendorReviews v where v.vendors.id = :id")
	public double avgVendorReview(@Param("id") int id);
	
	@Query("FROM VendorReviews WHERE vendors.id = :id")
	public Page<VendorReviews>  findVendorReviewPage(@Param("id") int id, Pageable pageable);

	@Query("FROM VendorReviews WHERE vendors.id = :vendorId AND users.id = :userId")
	public VendorReviews  findVendorReviewByUserAndVendorId(@Param("id") int vnedorId,@Param("id") int userId);
}
