package com.easycar.repo;


import com.easycar.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin,String> {

}
