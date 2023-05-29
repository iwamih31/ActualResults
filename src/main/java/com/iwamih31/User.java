package com.iwamih31;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

  // ID
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;

  // 部屋番号
  @Column(name = "room", nullable = false)
  private Integer room;

  // 名前
  @Column(name = "name", nullable = false)
  private String name;

  // 生年月日
  @DateTimeFormat(pattern = "yyyy-MM-dd")   // 入力時の期待フォーマット
  @JsonFormat(pattern = "yyyy/MM/dd")   // 出力時の期待フォーマット
  @Column(name = "birthday", nullable = true)
  private LocalDate birthday;

  // 介護度
  @Column(name = "level", nullable = false)
  private String level;

  // 入居日
  @DateTimeFormat(pattern = "yyyy-MM-dd")   // 入力時の期待フォーマット
  @JsonFormat(pattern = "yyyy/MM/dd")   // 出力時の期待フォーマット
  @Column(name = "move_in", nullable = true)
  private LocalDate move_in;

  // 利用状況
  @Column(name = "use", nullable = false)
  private String use;

  // 備考
  @Column(name = "note", nullable = false)
  private String note;
}
