package com.app.schedule;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class SqlString{
  private String sqlstr ="";

  public SqlString() {

  }

  public SqlString(String sqlstr) {
      this.sqlstr = sqlstr;
  }

  public void setColumnValue(String columnName ,String columnValue){
      if(this.sqlstr.indexOf("^"+columnName) == -1){
          System.out.println("no such column!") ;
      }
      else{
          int idxOfClmn = this.sqlstr.indexOf("^"+columnName);
          this.sqlstr = this.sqlstr.substring(0,idxOfClmn) + columnValue + this.sqlstr.substring(idxOfClmn+columnName.length()+1) ;
      }
  }

  public void setSql(String sqlstr){
      this.sqlstr = sqlstr;
  }

  public String getSql(){
      return this.sqlstr ;
  }

  public void printSql(){
     System.out.println(this.sqlstr) ;
  }
}