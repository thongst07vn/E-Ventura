package com.eventura.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventura.entities.Medias;
import com.eventura.entities.ProductCategories;
import com.eventura.entities.Products;
import com.eventura.repositories.CategoryRepository;
import com.eventura.repositories.MediaRepository;

@Service
public class MediaServiceImpl implements MediaService{

	@Autowired
	private MediaRepository mediaRepository;

	@Override
	public boolean save(Medias media) {
		// TODO Auto-generated method stub
		try {
			mediaRepository.save(media);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean delete(int id) {
		try {
			mediaRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
