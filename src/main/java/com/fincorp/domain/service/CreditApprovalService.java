package com.fincorp.domain.service;
public class CreditApprovalService{
public String evaluate(double salary,double amount){
double max=salary*0.30;
return amount<=max?"APROBADO":"RECHAZADO";
}}