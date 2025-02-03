import React, {useEffect, useState} from "react";
import { useParams } from "react-router-dom";
import ApiService from "../../service/ApiService";
import ProductList from "../common/ProductList";
import Pagination from "../common/Pagination";
import '../../style/home.css'
import { all } from "axios";


const CategoryProductsPage = () => {

    const {categoryId} = useParams();
    const [products, setProducts] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPage, setTotalPage] = useState(0);
    const [error, setError] = useState(null);
    const itemPerPage = 8;

    useEffect(() => {
        fetchProducts();
    },[categoryId, currentPage]);

    const fetchProducts = async() => {
        try{
            
            const response = await ApiService.getAllProductsByCategoryId(categoryId);
            const allProducts = response.productList || [];

            setTotalPage(Math.ceil(allProducts.length / itemPerPage));
            // setProducts(allProducts.slice((currentPage - 1 * itemPerPage, currentPage * itemPerPage)));
            setProducts(allProducts);

                 }

        catch(error){
            setError(error.response?.data?.message || 'unable to fetch products by category id')
        }
    }

    return (
        <div className="home">
            {error ? (
                <p className="error-message">{error}</p>
            ):(
                <div>
                    <ProductList products={products}/>
                    <Pagination currentPage={currentPage}
                    totalPage={totalPage}
                    onPageChange={(page) => setCurrentPage(page)}/>
                </div>
            )}
        </div>
    )
}

export default CategoryProductsPage;