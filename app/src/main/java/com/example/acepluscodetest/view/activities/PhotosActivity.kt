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

package com.example.acepluscodetest.view.activities

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.acepluscodetest.R
import com.google.android.material.snackbar.Snackbar
import com.example.acepluscodetest.action
import com.example.acepluscodetest.data.model.Movie
import com.example.acepluscodetest.databinding.ActivityPhotosBinding
import com.example.acepluscodetest.snack
import com.example.acepluscodetest.view.adapters.PhotosListAdapter
import com.example.acepluscodetest.viewmodel.PhotosViewModel
import kotlinx.android.synthetic.main.activity_photos.*
import kotlinx.android.synthetic.main.toolbar_view_custom_layout.*

class PhotosActivity : BaseActivity() {

  private val toolbar: Toolbar by lazy { toolbar_toolbar_view as Toolbar }
  private var adapter = PhotosListAdapter(mutableListOf()) { movie -> displayConfirmation(movie) }
  private lateinit var viewModel: PhotosViewModel
  private lateinit var title: String
  private lateinit var binding: ActivityPhotosBinding

  override fun getToolbarInstance(): Toolbar? = toolbar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityPhotosBinding.inflate(layoutInflater)
    setContentView(binding.root)
    intent?.extras?.getString("title")?.let {
      title = it
    }
    viewModel = ViewModelProviders.of(this).get(PhotosViewModel::class.java)
    RecyclerView.adapter = adapter
    searchMovie()
  }

  private fun showLoading() {
    ProgressBar.visibility = View.VISIBLE
    RecyclerView.isEnabled = false
  }

  private fun hideLoading() {
    ProgressBar.visibility = View.GONE
    RecyclerView.isEnabled = true
  }

  private fun showMessage() {
    photoLayout.snack(getString(R.string.network_error), Snackbar.LENGTH_INDEFINITE) {
      action(getString(R.string.ok)) {
        searchMovie()
      }
    }
  }


  private fun displayConfirmation(photo: Movie) {
    photoLayout.snack("Add ${photo.title} to your list?", Snackbar.LENGTH_LONG) {
      action(getString(R.string.ok)) {
        viewModel.saveMovie(photo)
      }
    }
    //val addintent = Intent(this,MainActivity::class.java)
    //startActivity(addintent)
  }

  private fun searchMovie() {
    showLoading()
    viewModel.getPhotos().observe(this, Observer { movies ->
      hideLoading()
      if (movies == null) {
        showMessage()
      } else {
        adapter.setMovies(movies)
      }
    })
  }
}
