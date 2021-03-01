/* eslint-disable vue/no-parsing-error */
<template>
  <v-app>
    <v-card class="overflow-hidden">
      <v-app-bar
          absolute
          color="#fcb69f"
          dark
          shrink-on-scroll
          src="./assets/logo.png"
          scroll-target="#scrolling-techniques-2"
      >
        <template v-slot:img="{ props }">
          <v-img
              v-bind="props"
              gradient="to top right, rgba(255, 196, 255,.5), rgba(255, 209, 225,.8)"
          ></v-img>
        </template>

        <v-app-bar-title>


          진하의 방명록
        </v-app-bar-title>

        <v-spacer></v-spacer>
        <v-btn icon>
          <img src="./assets/profile.png" width="50px" height="37px" style="vertical-align: bottom"/>
        </v-btn>
      </v-app-bar>
      <v-sheet
          id="scrolling-techniques-2"
          class="overflow-y-auto pink lighten-5"
          max-height="700"
      >
        <v-container style="height: 800px">
          <v-spacer></v-spacer>
          <v-spacer></v-spacer>
          <v-spacer></v-spacer>
          <v-spacer></v-spacer>
          <br />
          <br />
          <br />
          <br />
          <br />
          <v-main style="background-color: rgb(245, 236, 255)">
            <v-data-table
                :headers="headers"
                :items="guestbooks"
                sort-by="calories"
                class="elevation-1"
                style="background-color: rgb(251, 247, 255)"
            >
              <template v-slot:top>
                <v-toolbar flat style="background-color: rgb(251, 247, 255)">
                  <v-toolbar-title style="color: rgb(226, 137, 234)">
                    소띠해 모두 화이팅✧٩( •́ ⌄ •́ ๑)</v-toolbar-title
                  >
                  <v-divider class="mx-4" inset vertical></v-divider>
                  <v-spacer></v-spacer>
                  <v-dialog v-model="dialog" max-width="500px">
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                          color="rgb(255, 91, 139)"
                          dark
                          class="mb-2"
                          v-bind="attrs"
                          v-on="on"
                      >
                        New Item
                      </v-btn>
                    </template>
                    <v-card>
                      <v-card-title>
                        <span class="headline">{{ formTitle }}</span>
                      </v-card-title>

                      <v-card-text>
                        <v-container>
                          <v-row>
                            <v-col cols="12" sm="6" md="4">
                              <v-text-field
                                  v-model="editedItem.subject"
                                  label="Subject"
                              ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" md="4">
                              <v-text-field
                                  v-model="editedItem.content"
                                  label="Content"
                              ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" md="4">
                              <v-text-field
                                  v-model="editedItem.createdAt"
                                  label="CreatedAt"
                              ></v-text-field>
                            </v-col>
                          </v-row>
                        </v-container>
                      </v-card-text>

                      <v-card-actions>
                        <v-btn color="blue darken-1" text @click="close">
                          Cancel
                        </v-btn>
                        <v-btn color="blue darken-1" text @click="save">
                          Save
                        </v-btn>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                  <v-dialog v-model="dialogDelete" max-width="500px">
                    <v-card>
                      <v-card-title class="headline"
                      >Are you sure you want to delete this
                        item?</v-card-title
                      >
                      <v-card-actions>
                        <v-btn color="blue darken-1" text @click="closeDelete"
                        >Cancel</v-btn
                        >
                        <v-btn
                            color="blue darken-1"
                            text
                            @click="deleteItemConfirm"
                        >OK</v-btn
                        >
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                </v-toolbar>
              </template>
              <template v-slot:item.actions="{ item }">
                <v-icon small class="mr-2" @click="editItem(item)">
                  mdi-pencil
                </v-icon>
                <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
              </template>
              <template v-slot:no-data>
                <v-btn color="primary" @click="initialize"> Reset </v-btn>
              </template>
            </v-data-table>
          </v-main>
        </v-container>
      </v-sheet>
    </v-card>
  </v-app>
</template>

<script>
import axios from 'axios';
export default {
  name: "App",
  props: [
    {
      picture_src: "./assets/logo.png",
    },
  ],
  components: {},
  data: () => ({
    dialog: false,
    dialogDelete: false,
    headers: [

      {text: "글제목", value: "subject"},
      {text: "글내용", value: "content"},
      {text: "등록일", value: "createdAt"},
      {text: "Actions", value: "actions", sortable: false},
    ],
    guestbooks: [],
    // 추가, 수정, 삭제시 현재 선택된 Row의 인덱스
    // 새로 값을 추가할 때는 -1이 default
    editedIndex: -1,

    // 수정중인 아이템을 담는 변수
    editedItem: {
      subject: '',
      content: '',
      createdAt: ''
    },

    // 수정, 추가를 취소할때 초기화를 위한 변수
    defaultItem: {
      subject: '',
      content: '',
      createdAt: ''
    }
  }),

//
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'New Item' : 'Edit Item'
    },
  },

  watch: {
    dialog(val) {
      console.log("dialog:", val)

      val || this.close()
    },
    dialogDelete(val) {
      console.log("dialogDelete:", val)
      val || this.closeDelete()
    },
  },

  created() {
    this.initialize()
  },

  methods: {
    initialize() {
      axios.get('http://localhost:8080/api/guestbooks')
          .then(res => {
            this.guestbooks = JSON.parse(JSON.stringify(res.data._embedded.guestbooks));
          });
    },

    //
    editItem(item) {
      this.editedIndex = this.guestbooks.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    deleteItem(item) {
      this.editedIndex = this.guestbooks.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true

      axios.delete(item._links.self.href, {});
    },

    deleteItemConfirm() {
      this.guestbooks.splice(this.editedIndex, 1)
      this.closeDelete()
    },

    close() {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    closeDelete() {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    save() {
      if (this.editedIndex > -1) {
        axios.patch(this.editedItem._links.self.href, this.editedItem);
        Object.assign(this.guestbooks[this.editedIndex], this.editedItem)
      } else {

        this.guestbooks.push(this.editedItem)
        axios.post('http://localhost:8080/api/guestbooks', this.editedItem);
      }
      this.close()
    },
  },
}
</script>