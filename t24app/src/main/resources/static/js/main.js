Vue.component('todo-item', {
  template: '<li>This is a todo</li>'
});

var app = new Vue({
    el: '#app',
    data: {
        message: 'Script says hi!'
    }
});