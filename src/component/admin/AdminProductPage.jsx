import React, {useEffect, useState} from "react";
import ApiService from "../../service/ApiService";
import { useNavigate } from "react-router-dom"; 
import '../../style/adminProduct.css'
import Pagination from "../common/Pagination";


const AdminProductPage = () => {
    const navigate = useNavigate();
    const [products, setProducts] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [error, setError] = useState(null);
    const itemPerPage = 10;

    const fetchProducts = async() =>{
        try{
            const response = await ApiService.getAllProducts();
            const productList = response.productList || [];

            setTotalPages(Math.ceil(productList.length/itemPerPage));
            setProducts(productList.slice((currentPage - 1) * itemPerPage, currentPage * itemPerPage));

        }catch(error){
             setError(error.response?.data?.message || error.message || 'unable to fetch products')
        }
    }

    useEffect(() =>{
        fetchProducts();
    }, [currentPage])

    const handleEdit = async (id) => {
           navigate(`/admin/edit-product/${id}`)
        }

     const handleDelete = async (id) => {
            const confirmed = window.confirm("Are you sure you want to delete this product? ")
            if(confirmed){
                try{
                    await ApiService.deleteProduct(id);
                    fetchProducts();
                }catch(error){
                    console.log("Error deleting category by id")
                }
            }
        }
        

            return(
                <div className="admin-product-page">
                       {error ? (
                        <p className="error-message"></p>
                       ):(
                        <div>
                            <h2>Products</h2>
                            <button className="product-btn" onClick={() => {navigate('/admin/add-product')}}>Add product</button>
                             <ul>
                                {products.map((product) =>(
                                    <li key={product.id}>
                                        <span>{product.name}</span>
                                        <button className="product-btn" onClick={() => handleEdit(product.id)}>Edit</button>
                                        <button className="product-btn-delete" onClick={() => handleDelete(product.id)}>Delete</button>
                                    </li>
                                ))}
                             </ul>
                             <Pagination
                             currentPage={currentPage}
                             totalPage={totalPages}
                             onPageChange={(page) => setCurrentPage(page)}/>
                        </div>
                       )}
                </div>
            )
        }
        export default AdminProductPage;
