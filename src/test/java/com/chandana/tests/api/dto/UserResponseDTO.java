package com.chandana.tests.api.dto;
import com.chandana.tests.api.dto.UserDataDTO;
import java.util.List;


public class UserResponseDTO {

    private int page;
    private int per_page;
    private int total;
    private int total_pages;
    private List<UserDataDTO> data;
   


    public int getPage() {
        return page;
    }

    public int getPer_page() {
        return per_page;
    }

    public int getTotal() {
        return total;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public List<UserDataDTO> getData() {
        return data;
    }
}