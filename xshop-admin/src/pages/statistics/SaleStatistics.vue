<template>
    <div>
        <h3>日订单统计</h3>
        <ve-line :data="chartData1" width="100%" :events="chartEvents"></ve-line>
        <h3>日销量统计</h3>
        <ve-line :data="chartData" width="100%" :events="chartEvents"></ve-line>
    </div>
</template>

<script>
    import http from '../../util/HttpUtil'

    export default {
        name: "SaleStatistics",
        created() {
            this.getOrderStatistics()
        },
        data() {
            return {
                chartData1: {
                    columns: ['时间', '订单数'],
                    rows: []
                },
                chartData: {
                    columns: ['日期', '访问用户', '下单用户'],
                    rows: [
                        {'日期': '2018-05-22', '访问用户': 32371, '下单用户': 19810},
                        {'日期': '2018-05-23', '访问用户': 12328, '下单用户': 4398},
                        {'日期': '2018-05-24', '访问用户': 92381, '下单用户': 52910}
                    ]
                }
            }
        },
        methods: {
            getOrderStatistics() {
                const that = this;
                http.get(this, 'statistics', 'orderStatistics?type=0', function (resp) {
                    const data = JSON.parse(resp.body.data);
                    console.log(data)
                    let rows = []
                    for (let i = 0; i < data.length; i++) {
                        const obj = {}
                        obj['时间'] = data[i].time
                        obj['订单数'] = data[i].orderNumber

                        rows.push(obj)
                    }
                    console.log(rows)
                    that.$set(that.$data.chartData1, 'rows', rows)
                })
            }
        }
    }
</script>

<style scoped>

</style>