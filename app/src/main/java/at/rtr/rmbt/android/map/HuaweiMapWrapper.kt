package at.rtr.rmbt.android.map

import android.content.Context
import at.rmbt.client.control.data.MapStyleType
import at.rtr.rmbt.android.map.wrapper.*
import at.rtr.rmbt.android.util.iconFromVector
import com.huawei.map.mapapi.CameraUpdateFactory
import com.huawei.map.mapapi.HWMap
import com.huawei.map.mapapi.model.CircleOptions
import com.huawei.map.mapapi.model.MarkerOptions
import com.huawei.map.mapapi.model.TileOverlayOptions
import com.huawei.map.mapapi.model.TileProvider.NO_TILE
import timber.log.Timber

class HuaweiMapWrapper(private val huaweiMap: HWMap) : MapWrapper {

    override fun moveCamera(latLngW: LatLngW, zoom: Float) {
        huaweiMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngW.toHMSLatLng(), zoom))
    }

    override fun animateCamera(latLngW: LatLngW) {
        huaweiMap.animateCamera(CameraUpdateFactory.newLatLng(latLngW.toHMSLatLng()))
    }

    override fun animateCamera(latLngW: LatLngW, zoom: Float) {
        huaweiMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngW.toHMSLatLng(), zoom))
    }

    override fun addTileOverlay(tileWProvider: TileWrapperProvider): TileOverlayWrapper {
        val overlay = huaweiMap.addTileOverlay(TileOverlayOptions().tileProvider { x, y, zoom ->
            return@tileProvider try {
                tileWProvider.getTileW(x, y, zoom).toHMSTile()
            } catch (e: Exception) {
                Timber.e(e, "Failed to load tile")
                NO_TILE
            }
        })
        return HMSOverlayWrapper(overlay)
    }

    override fun addMarker(
        context: Context,
        latLngW: LatLngW,
        anchorU: Float,
        anchorV: Float,
        iconId: Int
    ): MarkerWrapper {
        val marker = huaweiMap.addMarker(
            MarkerOptions()
                .position(latLngW.toHMSLatLng())
                .anchor(anchorU, anchorV)
                .iconFromVector(context, iconId)
        )
        return HMSMarker(marker)
    }

    override fun setMyLocationEnabled(enabled: Boolean) {
        huaweiMap.isMyLocationEnabled = enabled
        huaweiMap.uiSettings.isMyLocationButtonEnabled = false
    }

    override fun currentCameraZoom(): Float {
        return huaweiMap.cameraPosition?.zoom ?: 1f
    }

    override fun setOnMapClickListener(listener: (latlngW: LatLngW) -> Unit) {
        huaweiMap.setOnMapClickListener {
            listener.invoke(LatLngW(it.latitude, it.longitude))
        }
    }

    override fun setOnCameraChangeListener(listener: (latlngW: LatLngW, currentZoom: Float) -> Unit) {
        huaweiMap.setOnCameraMoveListener {
            val ll = huaweiMap.cameraPosition.target
            listener.invoke(LatLngW(ll.latitude, ll.longitude), huaweiMap.cameraPosition.zoom)
        }
    }

    override fun setMapStyleType(style: MapStyleType) {
        huaweiMap.mapType = when (style) {
            MapStyleType.STANDARD -> HWMap.MAP_TYPE_NORMAL
            MapStyleType.SATELLITE -> HWMap.MAP_TYPE_SATELLITE
            MapStyleType.HYBRID -> HWMap.MAP_TYPE_HYBRID
        }
    }

    override fun addCircle(
        latLngW: LatLngW,
        fillColor: Int,
        strokeColor: Int,
        strokeWidth: Float,
        circleRadius: Double
    ) {
        huaweiMap.addCircle(
            CircleOptions()
                .center(latLngW.toHMSLatLng())
                .fillColor(fillColor)
                .strokeColor(strokeColor)
                .strokeWidth(strokeWidth)
                .radius(circleRadius)
        )
    }

    override fun supportSatelliteAndHybridView(): Boolean {
        return false
    }

}