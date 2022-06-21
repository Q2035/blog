<template>
  <div class="BlogIndex">
    <div class="m-container m-padded-tb-big">
      <div class="ui container">
        <div class="ui stackable grid">
          <div class="eleven wide column">
            <h1>{{ msg }}</h1>
            <!--content-->
            <div class="ui attached segment">
              <div
                class="ui padded vertical segment m-padded-tb-large"
                th:each="blog : ${page.content}"
              >
                <div class="ui middle aligned mobile reversed stackable grid">
                  <div class="eleven wide column">
                    <h3 class="ui header">
                      <a
                        href="#"
                        th:href="@{/blog/{id}(id=${blog.id})}"
                        class="m-black"
                        th:text="${blog.title}"
                        >你真的理解什么是财富自由吗？</a
                      >
                    </h3>
                    <p
                      style="margin-left: 10px !important"
                      class="m-text"
                      th:text="|${blog.description}...|"
                    >
                      正确做好任何一件事情的前提是清晰、正确的理解目标。而事实是，我们很多人很多时候根本没有对目标正确的定义，甚至根本从来就没有想过，只是大家都那么做而已…...
                    </p>
                    <br />
                    <div class="ui grid">
                      <div class="eleven wide column">
                        <div class="ui mini horizontal link list">
                          <div class="item">
                            <img
                              src="https://unsplash.it/100/100?image=1005"
                              th:src="@{${blog.userAvatar}}"
                              alt=""
                              class="ui avatar image"
                            />
                            <div class="content">
                              <a
                                href=""
                                th:href="@{/about}"
                                class="header"
                                th:text="${blog.nickname}"
                                >李仁密</a
                              >
                            </div>
                          </div>
                          <div class="item">
                            <i class="calendar icon"></i
                            ><span
                              th:text="${#dates.format(blog.createTime,'yyyy-MM-dd')}"
                              >2017-10-01</span
                            >
                          </div>
                          <div class="item">
                            <i class="eye icon"></i
                            ><span th:text="${blog.views}">2342</span>
                          </div>
                        </div>
                      </div>
                      <div class="right aligned five wide column">
                        <a
                          href="#"
                          class="ui teal basic label m-padded-tiny m-text-thin"
                          th:text="${blog.typeName}"
                          >认知升级</a
                        >
                      </div>
                    </div>
                  </div>

                  <div class="five wide column">
                    <a href="#" th:href="@{/blog/{id}(id=${blog.id})}">
                      <img
                        th:src="@{${blog.firstPicture}}"
                        src="https://unsplash.it/800/450?image=1005"
                        alt=""
                        class="ui rounded image"
                      />
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "BlogIndex",
  data() {
    return {
      msg: "Hello",
    };
  },
};
</script>

<style scoped>
@import url("https://cdn.hellooooo.top/static/css/temp.css");
</style>