package com.easycar.service;

import com.easycar.dto.AdminDTO;

import java.util.ArrayList;

public interface AdminService {
    void saveAdmin(AdminDTO dto);
    void updateAdmin(AdminDTO dto);
    void deleteAdmin(String reg_ID);
    ArrayList<AdminDTO> getAllAdmin();
}
