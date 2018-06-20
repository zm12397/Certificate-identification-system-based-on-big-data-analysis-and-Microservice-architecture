package com.zfm.gleaning.service;

import org.springframework.data.domain.Page;

import com.zfm.gleaning.pojo.ThanksLettersDO;

public interface ThanksLettersService {

	Page<ThanksLettersDO> listNewLetters(Integer news);

	Page<ThanksLettersDO> listPageLetters(Integer page, Integer rows);

	void addThanksLetters(Integer id);

}
