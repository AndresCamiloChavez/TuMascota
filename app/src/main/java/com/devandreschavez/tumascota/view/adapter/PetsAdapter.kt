package com.devandreschavez.samaca.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devandreschavez.tumascota.R
import com.devandreschavez.tumascota.data.models.PetUser
import com.devandreschavez.tumascota.databinding.ItemPetBinding
import com.devandreschavez.tumascota.view.ui.home.PetsFragment

class PetsAdapter(private var listPets: List<PetUser>, private val listener: onPetClickListener): RecyclerView.Adapter<BaseViewHolder<*>>() {


    interface onPetClickListener{
        fun onItemClick(item: PetUser)
        fun onSharePostPet( name: String, description: String, img:String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return PetsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent,false ))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is PetsViewHolder -> holder.bind(listPets[position], position)
        }
    }
    override fun getItemCount(): Int = listPets.size

    inner class PetsViewHolder(itemView: View): BaseViewHolder<PetUser>(itemView) {
        private val binding = ItemPetBinding.bind(itemView)
        override fun bind(item: PetUser, position: Int) {
            itemView.setOnClickListener {
                listener.onItemClick(item)
            }

            binding.tvNamePet.text = item.pet.namePet
            binding.tvSector.text = "Última vez en ${item.pet.sector}"
            Glide.with(itemView.context).load(item.pet.pictureAnimal).into(binding.imgPet)
            binding.btnShare.setOnClickListener {
                listener.onSharePostPet(item.pet.namePet, item.pet.description, item.pet.pictureAnimal)
            }
            binding.chipDate.text = item.pet.date.toString()
            binding.tvDescriptionPet.text = item.pet.description
        }
    }
}