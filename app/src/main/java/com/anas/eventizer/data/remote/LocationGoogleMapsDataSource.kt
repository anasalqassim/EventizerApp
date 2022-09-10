package com.anas.eventizer.data.remote

import android.graphics.Bitmap
import com.anas.eventizer.data.PlaceHasNoImagesException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.kiwimob.firestore.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationGoogleMapsDataSource @Inject constructor(
    private val placesClient: PlacesClient
) {

    companion object{
        const val MAX_NUMBER_OF_IMAGES = 3
        val PLACE_FIELDS = listOf(Place.Field.PHOTO_METADATAS,Place.Field.NAME)
        const val PLACE_HAS_NO_IMG_EXCEPTION_MSG = "IMAGE_HAS_IMGS"
    }


    /**
     * get MAX_NUMBER_OF_IMAGES for a place
     * and throws PlaceHasNoImagesException if the place has no images
     */
    suspend fun getPlaceImages(placeId:String):Flow<List<Bitmap>>
        = flow {
        val placeRequest = FetchPlaceRequest.newInstance(placeId, PLACE_FIELDS)
        val place = placesClient.fetchPlace(placeRequest)
            .await()
            .place

        val placePhotoMetadatas = place.photoMetadatas

        val photosBitmap = mutableListOf<Bitmap>()

        if (placePhotoMetadatas != null){
            if (placePhotoMetadatas.size > MAX_NUMBER_OF_IMAGES){
                for (photoMetadata in placePhotoMetadatas.slice(0 until MAX_NUMBER_OF_IMAGES)){
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata).build()
                    val photoResponse = placesClient.fetchPhoto(photoRequest).await()
                    photosBitmap.add(photoResponse.bitmap)
                }
            }else{
                for (photoMetadata in placePhotoMetadatas){
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata).build()
                    val photoResponse = placesClient.fetchPhoto(photoRequest).await()
                    photosBitmap.add(photoResponse.bitmap)
                }
            }

            emit(photosBitmap)
        }else{
            throw PlaceHasNoImagesException(PLACE_HAS_NO_IMG_EXCEPTION_MSG)
        }


    }



}