<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            @keydown.enter="$emit('close')"
            max-width="900"
    >
        <v-card>
            <v-card-title>
                <v-toolbar flat color="white">
                    <v-btn outlined class="mr-4" color="grey darken-2" @click="setToday">
                        Today
                    </v-btn>
                    <v-btn fab text small color="grey darken-2" @click="prev">
                        <v-icon small>mdi-chevron-left</v-icon>
                    </v-btn>
                    <v-btn fab text small color="grey darken-2" @click="next">
                        <v-icon small>mdi-chevron-right</v-icon>
                    </v-btn>
                    <v-toolbar-title>{{ title }}</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-menu bottom right>
                        <template v-slot:activator="{ on }">
                            <v-btn
                                    outlined
                                    color="grey darken-2"
                                    v-on="on"
                            >
                                <span>{{ typeToLabel[type] }}</span>
                                <v-icon right>mdi-menu-down</v-icon>
                            </v-btn>
                        </template>
                        <v-list>
                            <v-list-item @click="type = 'day'">
                                <v-list-item-title>Day</v-list-item-title>
                            </v-list-item>
                            <v-list-item @click="type = 'week'">
                                <v-list-item-title>Week</v-list-item-title>
                            </v-list-item>
                            <v-list-item @click="type = 'month'">
                                <v-list-item-title>Month</v-list-item-title>
                            </v-list-item>
                            <v-list-item @click="type = '4day'">
                                <v-list-item-title>4 days</v-list-item-title>
                            </v-list-item>
                        </v-list>
                    </v-menu>
                </v-toolbar>
            </v-card-title>
            <v-card-text style="height: 400px;">
                <v-calendar
                        ref="calendar"
                        v-model="focus"
                        color="primary"
                        :events="events"
                        :event-color="getEventColor"
                        :now="today"
                        :type="type"
                        @click:event="showEvent"
                        @click:more="viewDay"
                        @click:date="viewDay"
                        @change="updateRange"
                ></v-calendar>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>

                <v-btn
                        ref="btCloseDelete"
                        color="green darken-1"
                        text
                        @click="$emit('close')"
                >
                    Close
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>

    import JSOG from 'jsog';
    export default {
        name: "ClinicRoomAvailable",
        data: () => ({
            focus: '',
            type: 'month',
            typeToLabel: {
                month: 'Month',
                week: 'Week',
                day: 'Day',
                '4day': '4 Days',
            },
            today: null,
            start: null,
            end: null,
            selectedEvent: {},
            selectedElement: null,
            selectedOpen: false,
            events: [],
            colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'grey darken-1'],
            names: ['Meeting', 'Holiday', 'PTO', 'Travel', 'Event', 'Birthday', 'Conference', 'Party'],
        }),
        props: {
            value: {
                type: Boolean,
                default: false,
            },
            clinicRoom: null,
        },
        computed: {
            title () {
                const { start, end } = this;
                if (!start || !end) {
                    return ''
                }

                const startMonth = this.monthFormatter(start);
                const endMonth = this.monthFormatter(end);
                const suffixMonth = startMonth === endMonth ? '' : endMonth;

                const startYear = start.year;
                const endYear = end.year;
                const suffixYear = startYear === endYear ? '' : endYear;

                const startDay = start.day + this.nth(start.day);
                const endDay = end.day + this.nth(end.day);

                switch (this.type) {
                    case 'month':
                        return `${startMonth} ${startYear}`;
                    case 'week':
                    case '4day':
                        return `${startMonth} ${startDay} ${startYear} - ${suffixMonth} ${endDay} ${suffixYear}`;
                    case 'day':
                        return `${startMonth} ${startDay} ${startYear}`;
                }
                return '';
            },
            monthFormatter () {
                return this.$refs.calendar.getFormatter({
                    timeZone: 'UTC', month: 'long',
                });
            },
        },
        methods: {
            viewDay ({ date }) {
                this.focus = date;
                this.type = 'day';
            },
            getEventColor (event) {
                return event.color
            },
            setToday () {
                this.focus = this.today
            },
            prev () {
                this.$refs.calendar.prev()
            },
            next () {
                this.$refs.calendar.next()
            },
            showEvent ({ nativeEvent, event }) {
                const open = () => {
                    this.selectedEvent = event;
                    this.selectedElement = nativeEvent.target;
                    setTimeout(() => this.selectedOpen = true, 10);
                };

                if (this.selectedOpen) {
                    this.selectedOpen = false;
                    setTimeout(open, 10);
                } else {
                    open()
                }

                nativeEvent.stopPropagation()
            },
            updateRange ({ start, end }) {
                const events = [];

                for(let intervention of JSOG.decode(this.clinicRoom.interventions)) {

                    let startDateTime = new Date(intervention.dateTime.start);
                    let endDateTime = new Date(intervention.dateTime.end);
                    events.push({
                        start: this.formatDate(startDateTime, true),
                        end: this.formatDate(endDateTime, true),
                        name: 'Intervention',
                        color: 'primary'
                    });
                }

                this.start = start;
                this.end = end;
                this.events = events;
            },
            nth (d) {
                return d > 3 && d < 21
                    ? 'th'
                    : ['th', 'st', 'nd', 'rd', 'th', 'th', 'th', 'th', 'th', 'th'][d % 10];
            },
            formatDate (a, withTime) {
                return withTime
                    ? `${a.getFullYear()}-${a.getMonth() + 1}-${a.getDate()} ${a.getHours()}:${a.getMinutes()}`
                    : `${a.getFullYear()}-${a.getMonth() + 1}-${a.getDate()}`;
            },
        }
    }
</script>

<style scoped>

</style>
