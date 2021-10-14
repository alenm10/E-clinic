<template>
    <div :id="mapId"
         style="width: 100%; height: 100%; z-index: 0;"
    >

    </div>
</template>

<script>
    import 'leaflet/dist/leaflet.css';
    import L from 'leaflet';

    export default {
        name: "MapView",
        data: () => ({
            map: null,
            currentMarker: null,
            isActive: false,
            formerInput: null,
        }),
        props: {
            mapId: {
                type: String,
                default: 'map'
            },
            value: null,
            clickable: {
                type: Boolean,
                default: false
            },
            useAddress: {
                type: Boolean,
                default: false
            },
            address: {
                type: String,
                default: null
            }
        },
        watch: {
            address(newAddress, oldAddress) {
                if (!newAddress || newAddress.length === 0) {
                    this.resetMap();
                } else if (newAddress !== oldAddress) {
                    this.coordsFromAddress(newAddress);
                }
            },
            value(newCoords, oldCoords) {
                if (!newCoords) return;
                if(newCoords === oldCoords) return;
                this.placeMarker(newCoords);
                if (this.formerInput &&
                    this.formerInput.lat === newCoords.lat &&
                    this.formerInput.lng === newCoords.lng) return;
                this.map.setView([newCoords.lat, newCoords.lng], 15);
            }
        },
        mounted() {
            delete L.Icon.Default.prototype._getIconUrl;
            L.Icon.Default.mergeOptions({
                iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
                iconUrl: require("leaflet/dist/images/marker-icon.png"),
                shadowUrl: require("leaflet/dist/images/marker-shadow.png")
            });
            let mapOptions = {};
            if(!this.clickable) {
                mapOptions.zoomControl = false;
                mapOptions.touchZoom = false;
                mapOptions.doubleClickZoom = false;
                mapOptions.scrollWheelZoom = false;
                mapOptions.boxZoom = false;
                mapOptions.keyboard = false;
                mapOptions.dragging = false;
            }
            this.map = L.map(this.mapId, mapOptions).setView([0, 0], 2);
            let options = {
                maxZoom: 19,
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            };
            this.tileLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', options)
                .addTo(this.map);
            if (this.clickable) this.map.on('click', this.addMarker);
            if (this.value) {
                this.placeMarker(this.value);
                this.map.setView([this.value.lat, this.value.lng], 15);
            }
            this.$nextTick(() => this.map.invalidateSize());
        },
        updated() {
            // setTimeout(() => this.map.invalidateSize(), 500);
            this.$nextTick(() => this.map.invalidateSize());
        },
        methods: {
            addMarker(e) {
                this.placeMarker(e.latlng);
                this.formerInput = e.latlng;
                this.$emit('input', e.latlng);
            },
            placeMarker(latlng) {
                if (this.currentMarker !== null)
                    this.map.removeLayer(this.currentMarker);
                this.currentMarker = L.marker(latlng);
                this.currentMarker.addTo(this.map);
                if (this.useAddress) this.addressFromCoords(latlng);
            },
            async addressFromCoords(latlng) {
                try {
                    let res = await this.reverseGeoCode(latlng);
                    let address = res.data.display_name;

                    if (!address) this.$emit('clickAddress', null);
                    else this.$emit('clickAddress', [res.data.display_name]);
                } catch(err) {
                    console.log(err.response);
                }
            },
            async coordsFromAddress(newAddress) {
                try {
                    let res = await this.geoCode(newAddress);
                    let latlng = {lat: Number(res.data[0].lat), lng: Number(res.data[0].lon)};
                    let addresses = res.data.map(el => el.display_name);
                    this.placeMarker(latlng);
                    this.map.flyTo(new L.LatLng(latlng.lat, latlng.lng), 15);
                    this.$emit('clickAddress', addresses);
                    this.$emit('input', latlng);
                } catch (err) {
                    this.$emit('clickAddress', null);
                }
            }
            ,
            reverseGeoCode(latlng) {
                let query = `reverse?format=jsonv2&lat=${latlng.lat}&lon=${latlng.lng}`;
                return this.$axios.get(`https://nominatim.openstreetmap.org/${query}`);
            },
            geoCode(address) {
                let query = `?q=${address}&format=jsonv2&addressdetails=1&limit=6`;
                return this.$axios.get(`https://nominatim.openstreetmap.org/${query}`);
            },
            resetMap() {
                if (this.currentMarker) this.map.removeLayer(this.currentMarker);
                this.currentMarker = null;
                this.formerInput = null;
                this.map.setView([0, 0], 2);
                this.map.invalidateSize();
            }
        }
    }
</script>

<style scoped>
</style>
