/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.acepluscodetest.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.acepluscodetest.data.db.PhotoDao
import com.example.acepluscodetest.data.model.Movie
import com.example.acepluscodetest.data.model.PhotosResponse
import com.example.acepluscodetest.data.net.RetrofitClient
import com.example.acepluscodetest.db
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread


class PhotoRepositoryImpl : PhotoRepository {

  private val movieDao: PhotoDao = db.movieDao()
  private val retrofitClient = RetrofitClient()
  private val allMovies: LiveData<List<Movie>>

  init {
    allMovies = movieDao.getAll()
  }

  /*
  override fun deleteMovie(photo: Movie) {
    thread {
      db.movieDao().delete(photo.id)
    }
  }

   */

  //override fun getSavedMovies() = allMovies

  override fun saveMovie(photo: Movie) {
    thread {
      movieDao.insert(photo)
    }
  }

  override fun getPhotos(): LiveData<List<Movie>?> {

    val data = MutableLiveData<List<Movie>>()

    retrofitClient.getPhotos().enqueue(object : Callback<PhotosResponse> {
      override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
        data.value = null
        Log.d(this.javaClass.simpleName, "Failure")
      }

      override fun onResponse(call: Call<PhotosResponse>, response: Response<PhotosResponse>) {
        data.value = response.body()?.results
        Log.d(this.javaClass.simpleName, "Response: ${response.body()?.results}")
      }
    })
    return data
  }
}