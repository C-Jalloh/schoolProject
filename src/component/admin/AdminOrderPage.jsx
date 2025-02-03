
import React, {useEffect, useState} from "react";
import ApiService from "../../service/ApiService";
import { useNavigate } from "react-router-dom"; 
import Pagination from "../common/Pagination";
import '../../style/adminOrderPage.css';


const orderStatus = ["PENDING","CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED", "RETURNED"]

const AdminOrderPage = () => {

    const [orders, setOrders] = useState([])
    const [filteredOrders, setFilteredOrders] = useState([]);
    const [statusFilter, setStatusFilter] = useState('')
    const [searchStatus, setSearchStatus] = useState('')

    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [error, setError] = useState(null);
    const itemPerPage = 10;

    const navigate = useNavigate();

    useEffect(()=>{
        fetchOrders();
     },[searchStatus,currentPage])



     const fetchOrders = async() => {
     try{
        let response;
      if(searchStatus){
            response = await ApiService.getAllOrderItemByStatus(searchStatus)
      }else{
           response = await ApiService.getAllOrders()
      }
      const orderList = response.orderItemList || [];

      setTotalPages(Math.ceil(orderList.length/itemPerPage))
      setOrders(orderList);
      setFilteredOrders(orderList.slice((currentPage - 1) * itemPerPage, currentPage * itemPerPage))



     }catch(error){
        setError(error.response?.data?.message || error.message || 'unable to fetch orders')
        setTimeout(() => (
            setError('')
        ),3000)
     }
     }

     const handleFilterChange = (e) => {
        const filterValue = e.target.value;
        setStatusFilter(filterValue);
        setCurrentPage(1);

        if(filterValue){
            const filtered = orders.filter(order => order.status === filterValue)
            setFilteredOrders(filtered.slice(0, itemPerPage));
            setTotalPages(Math.ceil(filtered.length / itemPerPage))
        }else{
            setFilteredOrders(orders.slice(0, itemPerPage))
            setTotalPages(Math.ceil(orders.length / itemPerPage))
        }
     }

     const handleSearchStatusChange = async (e) =>{
        setSearchStatus(e.target.value);
        setCurrentPage(1)
     }

     const handleOrderDetails = (id) =>{
        navigate(`/admin/order-details/${id}`)
     }


     return(
        <div className="admin-orders-page">
            <h2>Orders</h2>
            {error && <p className="error-message">{error}</p>}
            <div className="filter-container">
                <div className="statusFilter">
                    <label htmlFor="Filter By Status"></label>
                    <select value={statusFilter} onChange={handleFilterChange}>
                        <option value="">All</option>
                        {orderStatus.map(status =>(
                            <option key={status} value={status}>{status}</option>
                        ))}
                    </select>
                </div>
                <div className="searchStatus">
                    <label>Search By Status</label>
                    <select value={searchStatus} onChange={handleSearchStatusChange}>
                        <option value="">All</option>
                        {orderStatus.map(status =>(
                            <option key={status} value={status}>{status}</option>
                        ))}
                    </select>
                </div>
            </div>

            <table className="orders-table">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Customer</th>
                        <th>Status</th>
                        <th>Price</th>
                        <th>Date Ordered</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredOrders.map((order =>(
                        <tr key={order.id}>
                            <td>{order.id}</td>
                            {/* The order does not contain any user. That's why below is not working and I commented it */}
                            {/* <td>{order.user.name}</td> */}
                            <td>Static holder for the above</td>
                            <td>{order.status}</td>
                            <td>{order.price.toFixed(2)}</td>
                            <td>{new Date(order.createdAt).toLocaleDateString()}</td>
                            <td>
                                <button onClick={()=> handleOrderDetails(order.id)}>Details</button>
                            </td>
                        </tr>
                    )))}
                </tbody>
            </table>

            <Pagination 
            currentPage={currentPage}
            totalPage={totalPages}
            onPageChange={(page) => setCurrentPage(page)}/>
        </div>
     )
}
export default AdminOrderPage;