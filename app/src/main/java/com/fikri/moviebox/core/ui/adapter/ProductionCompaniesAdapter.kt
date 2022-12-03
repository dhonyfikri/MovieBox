package com.fikri.moviebox.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.moviebox.R
import com.fikri.moviebox.core.data.source.remote.network.Server
import com.fikri.moviebox.core.domain.model.ProductionCompanies
import com.fikri.moviebox.databinding.CompanyItemBinding

class ProductionCompaniesAdapter(private val listCompany: ArrayList<ProductionCompanies>) :
    RecyclerView.Adapter<ProductionCompaniesAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: CompanyItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            CompanyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val company = listCompany[position]
        Glide.with(holder.itemView.context)
            .load(Server.TMDB_IMAGE_BASE_URL + company.logoPath)
            .error(R.drawable.default_image)
            .into(holder.binding.ivLogo)
        holder.binding.tvCompanyName.text = company.name
    }

    override fun getItemCount() = listCompany.size
}