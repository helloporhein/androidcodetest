package com.example.acepluscodetest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.acepluscodetest.data.PhotoRepository
import com.example.acepluscodetest.data.PhotoRepositoryImpl
import com.example.acepluscodetest.data.model.Movie

class PhotosViewModel(private val repository: PhotoRepository = PhotoRepositoryImpl()): ViewModel()  {

  fun getPhotos(): LiveData<List<Movie>?> {
    return repository.getPhotos()
  }

  fun saveMovie(photo: Movie) {
    repository.saveMovie(photo)
  }
}