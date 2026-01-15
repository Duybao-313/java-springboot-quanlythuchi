package com.duybao.QUANLYCHITIEU.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtToken {
    private  String token;
    private Date ExpiryDate;
}
